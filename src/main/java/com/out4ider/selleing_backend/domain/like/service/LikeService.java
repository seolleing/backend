package com.out4ider.selleing_backend.domain.like.service;

import com.out4ider.selleing_backend.domain.like.repository.LikeCommentRepository;
import com.out4ider.selleing_backend.domain.like.repository.LikeNovelRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.AlreadyDoingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeCommentRepository likeCommentRepository;
    private final RedisTemplate<String,Long> stringLongRedisTemplate;
    private final Duration expireTime = Duration.ofHours(2);
    private final LikeNovelRepository likeNovelRepository;

    @Transactional
    public void likeNovel(Long id, Long userId) {
        SetOperations<String, Long> setOperations = stringLongRedisTemplate.opsForSet();
        setOperations.add("newLikeNovel:" + id, userId);
        stringLongRedisTemplate.expire("newLikeNovel:" + id, expireTime);
    }

    @Transactional
    public void unlikeNovel(Long id, Long userId) {
        SetOperations<String, Long> setOperations = stringLongRedisTemplate.opsForSet();
        if (Boolean.TRUE.equals(setOperations.isMember("oldLikeNovel:" + id, userId))) {
            setOperations.add("deleteLikeNovel:" + id, userId);
        }
        stringLongRedisTemplate.expire("deleteLikeNovel:" + id, expireTime);
        setOperations.remove("newLikeNovel:" + id, userId);
    }

    @Transactional
    public void likeComment(Long id, Long userId) {
        if (Boolean.TRUE.equals(stringLongRedisTemplate.hasKey("newLikeComment:" + id))
                || likeCommentRepository.findLikeComment(id, userId).isPresent()) {
            throw new AlreadyDoingException(ExceptionEnum.ALREADYDOING.ordinal(), "이미 좋아요를 누르셨습니다.", HttpStatus.FORBIDDEN);
        } else {
            SetOperations<String, Long> setOperations = stringLongRedisTemplate.opsForSet();
            setOperations.add("newLikeComment:" + id, userId);
            stringLongRedisTemplate.expire("newLikeComment:" + id, expireTime);
        }
    }
//    @Scheduled(fixedDelay = 3000000, initialDelay = 10000)
//    @Transactional
//    public void likeNovelCacheToDB() {
//        Set<String> newNovelIds = stringRedisTemplate.keys("newLikeNovel:*");
//        Map<Long, Set<String>> novelIdWithEmails = new HashMap<>();
//        if (newNovelIds != null) {
//            Set<Long> novelIds = newNovelIds.stream().map(novelId -> Long.valueOf(novelId.split(":")[1])).collect(Collectors.toSet());
//            for(Long novelId : novelIds) {
//                Set<String> emails = stringRedisTemplate.opsForSet().members("newLikeNovel:" + novelId);
//                novelIdWithEmails.put(novelId, emails);
//            }
//            likeNovelRepository.batchInsert(novelIds, novelIdWithEmails);
//        }
//    }
//
//    @Scheduled(fixedDelay = 3300000, initialDelay = 15000)
//    @Transactional
//    public void likeCommentCacheToDB() {
//        Set<String> newCommentIds = stringRedisTemplate.keys("newLikeComment:*");
//        Map<Long, Set<String>> commentIdWithEmails = new HashMap<>();
//        if (newCommentIds != null) {
//            Set<Long> commentIds = newCommentIds.stream().map(novelId -> Long.valueOf(novelId.split(":")[1])).collect(Collectors.toSet());
//            for (Long commentId : commentIds) {
//                Set<String> emails = stringRedisTemplate.opsForSet().members("newLikeComment:" + commentId);
//                commentIdWithEmails.put(commentId, emails);
//            }
//            likeCommentRepository.batchInsert(commentIds, commentIdWithEmails);
//        }
//    }
}
