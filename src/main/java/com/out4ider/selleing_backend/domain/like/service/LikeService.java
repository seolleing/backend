package com.out4ider.selleing_backend.domain.like.service;

import com.out4ider.selleing_backend.domain.like.repository.LikeCommentRepository;
import com.out4ider.selleing_backend.domain.like.repository.LikeNovelRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.AlreadyDoingException;
import com.out4ider.selleing_backend.global.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    private final LikeCommentRepository likeCommentRepository;
    private final RedisService redisService;
    private final LikeNovelRepository likeNovelRepository;
    @Value("${spring.batchSize}")
    private int batchSize;

    @Transactional
    public void likeNovel(Long novelId, Long userId) {
        if (redisService.checkValueExisting("deleteLikeNovel:"+novelId, userId)) {
            redisService.removeValue("deleteLikeNovel:"+novelId, userId);
        } else {
            redisService.addValue("newLikeNovel:"+novelId, userId);
        }
    }

    @Transactional
    public void unlikeNovel(Long novelId, Long userId) {
        if (redisService.checkValueExisting("newLikeNovel:"+novelId, userId)) {
            redisService.removeValue("newLikeNovel:"+novelId, userId);
        }
        redisService.addValue("deleteLikeNovel:"+novelId, userId);
    }

    @Transactional
    public void likeComment(Long id, Long userId) {
        if (redisService.checkValueExisting("newLikeComment:"+id, userId)
                || likeCommentRepository.findLikeComment(id, userId).isPresent()) {
            throw new AlreadyDoingException(ExceptionEnum.ALREADYDOING.ordinal(), "이미 좋아요를 누르셨습니다.", HttpStatus.FORBIDDEN);
        } else {
            redisService.addValue("newLikeComment:"+id, userId);
        }
    }

    @Scheduled(fixedDelay = 3000000, initialDelay = 3000000)
    @Transactional
    public void likeNovelCacheToDB() {
        Set<String> newNovelIds = redisService.getAllKey("newLikeNovel:*");
        List<Pair<Long,Long>> novelIdAndUserIdList = new ArrayList<>(100);
        if (newNovelIds != null) {
            Set<Long> novelIds = newNovelIds.stream().map(novelId -> Long.valueOf(novelId.split(":")[1])).collect(Collectors.toSet());
            for (Long novelId : novelIds) {
                Set<Long> userIds = redisService.getAllValue("newLikeNovel:" + novelId);
                for(Long userId : userIds){
                    novelIdAndUserIdList.add(Pair.of(novelId, userId));
                    if(novelIdAndUserIdList.size()==batchSize){
                        likeNovelRepository.batchInsert(novelIdAndUserIdList);
                    }
                }
            }
            if(!novelIdAndUserIdList.isEmpty()) {
                likeNovelRepository.batchInsert(novelIdAndUserIdList);
            }
            redisService.removeAllNewKey(newNovelIds);
        }
    }

    @Scheduled(fixedDelay = 3300000, initialDelay = 3300000)
    @Transactional
    public void likeCommentCacheToDB() {
        Set<String> newCommentIds = redisService.getAllKey("newLikeComment:*");
        List<Pair<Long, Long>> commentIdAndUserIdList = new ArrayList<>(100);
        if (newCommentIds != null) {
            Set<Long> commentIds = newCommentIds.stream().map(novelId -> Long.valueOf(novelId.split(":")[1])).collect(Collectors.toSet());
            for (Long commentId : commentIds) {
                Set<Long> userIds = redisService.getAllValue("newLikeComment:"+commentId);
                for (Long userId : userIds) {
                    commentIdAndUserIdList.add(Pair.of(commentId, userId));
                    if (commentIdAndUserIdList.size() == batchSize) {
                        likeCommentRepository.batchInsert(commentIdAndUserIdList);
                    }
                }
            }
            if(!commentIdAndUserIdList.isEmpty()){
                likeCommentRepository.batchInsert(commentIdAndUserIdList);
            }
            redisService.removeAllNewKey(newCommentIds);
        }
    }
}
