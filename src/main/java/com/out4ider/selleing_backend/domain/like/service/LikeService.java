package com.out4ider.selleing_backend.domain.like.service;

import com.out4ider.selleing_backend.domain.like.repository.LikeCommentRepository;
import com.out4ider.selleing_backend.domain.like.repository.LikeNovelRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.AlreadyDoingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeCommentRepository likeCommentRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final Duration expireTime = Duration.ofHours(2);
    private final LikeNovelRepository likeNovelRepository;

    @Transactional
    public void likeNovel(Long id, String email) {
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        setOperations.add("newLikeNovel:" + id, email);
        stringRedisTemplate.expire("newLikeNovel:" + id, expireTime);
    }

    @Transactional
    public void unlikeNovel(Long id, String email) {
        SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
        if (Boolean.TRUE.equals(setOperations.isMember("oldLikeNovel:" + id, email))) {
            setOperations.add("deleteLikeNovel:" + id, email);
        }
        stringRedisTemplate.expire("deleteLikeNovel:" + id, expireTime);
        setOperations.remove("newLikeNovel:" + id, email);
    }

    @Transactional
    public void likeComment(Long id, String email) {
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey("newLikeComment:" + id))
                || likeCommentRepository.findLikeComment(id, email).isPresent()) {
            throw new AlreadyDoingException(ExceptionEnum.ALREADYDOING.ordinal(), "이미 좋아요를 누르셨습니다.", HttpStatus.FORBIDDEN);
        } else {
            SetOperations<String, String> setOperations = stringRedisTemplate.opsForSet();
            setOperations.add("newLikeComment:" + id, email);
            stringRedisTemplate.expire("newLikeComment:" + id, expireTime);
        }
    }
    @Scheduled(fixedDelay = 3000000, initialDelay = 10000)
    @Transactional
    public void likeNovelCacheToDB() {
        Set<String> newNovelIds = stringRedisTemplate.keys("newLikeNovel:*");
        Map<Long, Set<String>> novelIdWithEmails = new HashMap<>();
        if (newNovelIds != null) {
            Set<Long> novelIds = newNovelIds.stream().map(novelId -> Long.valueOf(novelId.split(":")[1])).collect(Collectors.toSet());
            for(Long novelId : novelIds) {
                Set<String> emails = stringRedisTemplate.opsForSet().members("newLikeNovel:" + novelId);
                novelIdWithEmails.put(novelId, emails);
            }
            likeNovelRepository.batchInsert(novelIds, novelIdWithEmails);
        }
    }

    @Scheduled(fixedDelay = 3300000, initialDelay = 15000)
    @Transactional
    public void likeCommentCacheToDB() {
        Set<String> newCommentIds = stringRedisTemplate.keys("newLikeComment:*");
        Map<Long, Set<String>> commentIdWithEmails = new HashMap<>();
        if (newCommentIds != null) {
            Set<Long> commentIds = newCommentIds.stream().map(novelId -> Long.valueOf(novelId.split(":")[1])).collect(Collectors.toSet());
            for (Long commentId : commentIds) {
                Set<String> emails = stringRedisTemplate.opsForSet().members("newLikeComment:" + commentId);
                commentIdWithEmails.put(commentId, emails);
            }
            likeCommentRepository.batchInsert(commentIds, commentIdWithEmails);
        }
    }
}
