package com.out4ider.selleing_backend.domain.novel.service;

import com.out4ider.selleing_backend.domain.comment.repository.CommentRepository;
import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import com.out4ider.selleing_backend.domain.like.repository.LikeNovelRepository;
import com.out4ider.selleing_backend.domain.novel.dto.NovelRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelTotalResponseDto;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.out4ider.selleing_backend.domain.novel.repository.NovelInfoRepository;
import com.out4ider.selleing_backend.domain.novel.repository.NovelRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import com.out4ider.selleing_backend.global.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    public List<NovelResponseDto> getSome(Long lastId) {
        return novelRepository.findAllOrderByNovelId(lastId);
    }

    public List<NovelResponseDto> getSome2(Integer likeCount,Long lastId) {
        return novelRepository.findAllOrderByLikeCount(likeCount,lastId);
    }

    public NovelTotalResponseDto get(Long novelId, Long userId) {
        int likeCount;
        boolean isContainUserId;
        if (redisService.alreadyHasOldLikeNovelKey(novelId)) {
            List<LikeNovelEntity> likeNovelEntities = likeNovelRepository.findByNovelIdWithUser(novelId);
            List<Long> userIds = likeNovelEntities
                    .stream().map(likeNovelEntity -> likeNovelEntity.getUser().getUserId()).toList();
            isContainUserId = userIds.contains(userId);
            likeCount = likeNovelEntities.size();
            redisService.addOldLikeNovel(novelId, userIds.toArray(new Long[0]));
        } else {
            isContainUserId = redisService.checkValueExisting("oldLikeNovel:" + novelId, userId);
            likeCount = redisService.getSize("oldLikeNovel:" + novelId);
        }
        return new NovelTotalResponseDto(checkLiked(isContainUserId,novelId,userId), likeCount + redisService.getSize("newLikeNovel:" + novelId),
                novelInfoRepository.findByNovelId(novelId),
                commentRepository.findByNovelId(novelId));
    }

    @Transactional
    public void updateReport(Long novelId) {
        NovelEntity novelEntity = novelRepository.findById(novelId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        novelEntity.setReported(true);
        novelRepository.save(novelEntity);
    }

    public List<NovelResponseDto> getBookmarks(int page, Long userId) {
        return null;
    }

    private boolean checkLiked(boolean isContainUserId, Long novelId, Long userId) {
        return isContainUserId || redisService.checkValueExisting("newLikeNovel:" + novelId, userId);
    }
}
