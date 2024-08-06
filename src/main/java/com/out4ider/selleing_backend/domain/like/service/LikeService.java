package com.out4ider.selleing_backend.domain.like.service;

import com.out4ider.selleing_backend.domain.like.repository.LikeCommentRepository;
import com.out4ider.selleing_backend.domain.like.repository.LikeNovelRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.AlreadyDoingException;
import com.out4ider.selleing_backend.global.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeCommentRepository likeCommentRepository;
    private final RedisService redisService;
    private final LikeNovelRepository likeNovelRepository;

    @Transactional
    public void likeNovel(Long novelId, Long userId) {
        if (redisService.checkDeleteLikeNovel(novelId, userId)) {
            redisService.removeDeleteLikeNovelValue(novelId, userId);
        } else {
            redisService.addNewLikeNovel(novelId, userId);
        }
    }

    @Transactional
    public void unlikeNovel(Long novelId, Long userId) {
        if (redisService.checkNewLikeNovel(novelId, userId)) {
            redisService.removeNewLikeNovelValue(novelId, userId);
        }
        redisService.addDeleteLikeNovel(novelId, userId);
    }

    @Transactional
    public void likeComment(Long id, Long userId) {
        if (redisService.checkNewLikeComment(id, userId)
                || likeCommentRepository.findLikeComment(id, userId).isPresent()) {
            throw new AlreadyDoingException(ExceptionEnum.ALREADYDOING.ordinal(), "이미 좋아요를 누르셨습니다.", HttpStatus.FORBIDDEN);
        } else {
            redisService.addNewLikeComment(id, userId);
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
