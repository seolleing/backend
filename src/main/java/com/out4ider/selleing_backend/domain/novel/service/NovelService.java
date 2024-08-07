package com.out4ider.selleing_backend.domain.novel.service;

import com.out4ider.selleing_backend.domain.comment.repository.CommentRepository;
import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import com.out4ider.selleing_backend.domain.like.repository.LikeNovelRepository;
import com.out4ider.selleing_backend.domain.novel.dto.NovelRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelTotalResponseDto;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.entity.NovelInfoEntity;
import com.out4ider.selleing_backend.domain.novel.repository.NovelInfoRepository;
import com.out4ider.selleing_backend.domain.novel.repository.NovelRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import com.out4ider.selleing_backend.global.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NovelService {
    private final NovelRepository novelRepository;
    private final CommentRepository commentRepository;
    private final NovelInfoRepository novelInfoRepository;
    private final LikeNovelRepository likeNovelRepository;
    private final RedisService redisService;

    @Transactional
    public Long save(NovelRequestDto novelRequestDto) {
        NovelEntity novelEntity = NovelEntity.builder()
                .title(novelRequestDto.getTitle())
                .startSentence(novelRequestDto.getStartSentence())
                .likeNovels(new ArrayList<>())
                .isReported(false)
                .build();
        novelRepository.save(novelEntity);
        novelInfoRepository.batchInsert(novelRequestDto.getNovelInfoRequestDtos(), novelEntity.getNovelId());
        return novelEntity.getNovelId();
    }

    public List<NovelResponseDto> getSome(int page, String orderby) {
        Pageable pageable = null;
        List<NovelResponseDto> novelResponseDtos = null;
        if (orderby.equals("novelId")) {
            pageable = PageRequest.of(page, 10, Sort.by(orderby).descending());
            novelResponseDtos = novelRepository.findAllWithNovelId(pageable).stream().map(novelEntity -> {
                int newLikeCount = redisService.getSize("newLikeNovel:" + novelEntity.getNovelId());
                return novelEntity.toNovelResponseDto(newLikeCount);
            }).toList();
        } else {
            pageable = PageRequest.of(page, 10);
            novelResponseDtos = novelRepository.findAllWithLikeNovel(pageable).stream().map(novelEntity -> {
                int newLikeCount = redisService.getSize("newLikeNovel:" + novelEntity.getNovelId());
                return novelEntity.toNovelResponseDto(newLikeCount);
            }).toList();
        }
        return novelResponseDtos;
    }

    public NovelTotalResponseDto get(Long novelId, Long userId) {
        int likeCount;
        boolean isContainUserId;
        if (redisService.alreadyHasOldLikeNovelKey(novelId)) {
            List<LikeNovelEntity> likeNovelEntities = likeNovelRepository.findLikeNovel(novelId);
            List<Long> userIds = likeNovelEntities.stream().map(likeNovelEntity -> likeNovelEntity.getUser().getUserId()).toList();
            isContainUserId = userIds.contains(userId);
            likeCount = likeNovelEntities.size();
            redisService.addOldLikeNovel(novelId, userIds.toArray(new Long[0]));
        } else {
            isContainUserId = redisService.checkValueExisting("oldLikeNovel:" + novelId, userId);
            likeCount = redisService.getSize("oldLikeNovel:" + novelId);
        }
        return new NovelTotalResponseDto(checkLiked(isContainUserId,novelId,userId), likeCount + redisService.getSize("newLikeNovel:" + novelId), novelInfoRepository.findByNovelId(novelId).stream().map(NovelInfoEntity::toNovelInfoResponseDto).toList(),
                commentRepository.findByNovelId(novelId).stream().map(commentEntity -> {
                    int newLikeCount = redisService.getSize("newLikeComment:" + commentEntity.getId());
                    return commentEntity.toCommentResponseDto(newLikeCount);
                }).toList());
    }

    @Transactional
    public void updateReport(Long novelId) {
        NovelEntity novelEntity = novelRepository.findById(novelId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        novelEntity.setReported(true);
        novelRepository.save(novelEntity);
    }

//    public List<NovelResponseDto> getBookmarks(int page, String email) {
//        Pageable pageable = PageRequest.of(page, 10, Sort.by("novelId").descending());
//        return novelRepository.findAllWithLike(pageable, email).stream().map(novelEntity -> {
//            Long newLikeCount = stringLongRedisTemplate.opsForSet().size("newLikeNovel:" + novelEntity.getNovelId());
//            return novelEntity.toNovelResponseDto(newLikeCount == null ? 0 : newLikeCount.intValue());
//        }).toList();
//    }

    private boolean checkLiked(boolean isContainUserId, Long novelId, Long userId) {
        return isContainUserId && !redisService.checkValueExisting("deleteLikeNovel:" + novelId, userId)
                || !isContainUserId && redisService.checkValueExisting("newLikeNovel:" + novelId, userId);
    }
}
