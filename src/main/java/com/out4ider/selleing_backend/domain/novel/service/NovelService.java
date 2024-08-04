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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NovelService {
    private final NovelRepository novelRepository;
    private final CommentRepository commentRepository;
    private final NovelInfoRepository novelInfoRepository;
    private final LikeNovelRepository likeNovelRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final Duration expiredTime = Duration.ofHours(2);

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
                Long newLikeCount = stringRedisTemplate.opsForSet().size("newLikeNovel:" + novelEntity.getNovelId());
                return novelEntity.toNovelResponseDto(newLikeCount == null ? 0 : newLikeCount.intValue());
            }).toList();
        } else {
            pageable = PageRequest.of(page, 10);
            novelResponseDtos = novelRepository.findAllWithLikeNovel(pageable).stream().map(novelEntity -> {
                Long newLikeCount = stringRedisTemplate.opsForSet().size("newLikeNovel:" + novelEntity.getNovelId());
                return novelEntity.toNovelResponseDto(newLikeCount == null ? 0 : newLikeCount.intValue());
            }).toList();
        }
        return novelResponseDtos;
    }

    @Async
    public NovelTotalResponseDto get(Long novelId, String email) {
        boolean isLiked=false;
        int likeCount = 0;
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        if (!Boolean.TRUE.equals(stringRedisTemplate.hasKey("oldLikeNovel:" + novelId))) {
            List<LikeNovelEntity> likeNovelEntities = likeNovelRepository.findLikeNovel(novelId);
            List<String> emails = likeNovelEntities.stream().map(likeNovelEntity -> likeNovelEntity.getUser().getEmail()).toList();
            isLiked = emails.contains(email);
            likeCount = likeNovelEntities.size();
            setOperations.add("oldLikeNovel:" + novelId, emails.toArray(new String[0]));
            stringRedisTemplate.expire("oldLikeNovel:"+novelId, expiredTime);

        }else{
            isLiked = Boolean.TRUE.equals(setOperations.isMember("oldLikeNovel:" + novelId, email));
            likeCount= Objects.requireNonNull(setOperations.size("oldLikeNovel:" + novelId)).intValue();
        }
        return new NovelTotalResponseDto(isLiked, likeCount, novelInfoRepository.findByNovelId(novelId).stream().map(NovelInfoEntity::toNovelInfoResponseDto).toList(),
                commentRepository.findByNovelId(novelId).stream().map(commentEntity -> {
                    Long newLikeCount = stringRedisTemplate.opsForSet().size("newLikeComment:" + commentEntity.getId());
                    return commentEntity.toCommentResponseDto(newLikeCount == null ? 0 : newLikeCount.intValue());
                }).toList());
    }

    @Transactional
    public void updateReport(Long novelId) {
        NovelEntity novelEntity = novelRepository.findById(novelId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        novelEntity.setReported(true);
        novelRepository.save(novelEntity);
    }

    public List<NovelResponseDto> getBookmarks(int page, String email) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("novelId").descending());
        return novelRepository.findAllWithLike(pageable, email).stream().map(novelEntity -> {
            Long newLikeCount = stringRedisTemplate.opsForSet().size("newLikeNovel:" + novelEntity.getNovelId());
            return novelEntity.toNovelResponseDto(newLikeCount == null ? 0 : newLikeCount.intValue());
        }).toList();
    }
}
