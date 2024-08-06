package com.out4ider.selleing_backend.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Byte> stringByteRedisTemplate;
    private final RedisTemplate<String, Long> stringLongRedisTemplate;
    private final Duration expiredTime = Duration.ofHours(2);

    //like redis service
    public boolean checkDeleteLikeNovel(Long novelId, Long userId){
        return Boolean.TRUE.equals(stringLongRedisTemplate.opsForSet().isMember("deleteLikeNovel:" + novelId, userId));
    }

    public void removeDeleteLikeNovelValue(Long novelId, Long userId){
        stringLongRedisTemplate.opsForSet().remove("deleteLikeNovel:" + novelId, userId);
    }

    public void addNewLikeNovel(Long novelId, Long userId) {
        stringLongRedisTemplate.opsForSet().add("newLikeNovel:" + novelId, userId);
    }

    public boolean checkNewLikeNovel(Long novelId, Long userId) {
        return Boolean.TRUE.equals(stringLongRedisTemplate.opsForSet().isMember("newLikeNovel:" + novelId, userId));
    }

    public void removeNewLikeNovelValue(Long novelId, Long userId) {
        stringLongRedisTemplate.opsForSet().remove("newLikeNovel:" + novelId, userId);
    }

    public void addDeleteLikeNovel(Long novelId, Long userId) {
        stringLongRedisTemplate.opsForSet().add("deleteLikeNovel:" + novelId, userId);
    }

    public boolean checkNewLikeComment(Long commentId, Long userId) {
        return Boolean.TRUE.equals(stringLongRedisTemplate.opsForSet().isMember("newLikeComment:" + commentId, userId));
    }

    public void addNewLikeComment(Long commentId, Long userId) {
        stringLongRedisTemplate.opsForSet().add("newLikeComment:" + commentId, userId);
    }

    //novel redis service
    public int getSizeNewLikeNovel(Long novelId) {
        Long newLikeNovelSize = stringLongRedisTemplate.opsForSet().size("newLikeNovel:" + novelId);
        return newLikeNovelSize == null ? 0 : newLikeNovelSize.intValue();
    }

    public int getSizeNewLikeComment(Long commentId) {
        Long newLikeCommentSize = stringLongRedisTemplate.opsForSet().size("newLikeComment:" + commentId);
        return newLikeCommentSize == null ? 0 : newLikeCommentSize.intValue();
    }

    public boolean alreadyHasOldLikeNovelKey(Long novelId) {
        return Boolean.TRUE.equals(stringLongRedisTemplate.hasKey("oldLikeNovel:" + novelId));
    }

    public void addOldLikeNovel(Long novelId, Long[] userIds) {
        String keyName = "oldLikeNovel:" + novelId;
        stringLongRedisTemplate.opsForSet().add(keyName, userIds);
        stringLongRedisTemplate.expire(keyName, expiredTime);
    }

    public boolean checkUsersLike(Long novelId, Long userId) {
        return Boolean.TRUE.equals(stringLongRedisTemplate.opsForSet().isMember("oldLikeNovel:" + novelId, userId))
                || Boolean.TRUE.equals(stringLongRedisTemplate.opsForSet().isMember("newLikeNovel:" + novelId, userId));
    }

    public int getSizeOldLikeNovel(Long novelId) {
        return Objects.requireNonNull(stringLongRedisTemplate.opsForSet().size("oldLikeNovel:" + novelId)).intValue();
    }

    //gameRoom redis service
    public void setGameRoomInitialHeadCount(Long gameRoomId) {
        stringByteRedisTemplate.opsForValue().set("gameRoom:" + gameRoomId, (byte) 1);
    }

    public void deleteGameRoomHeadCount(Long gameRoomId) {
        stringByteRedisTemplate.delete("gameRoom:" + gameRoomId);
    }

    public void subGameRoomHeadCount(Long gameRoomId) {
        byte currentHeadCount = this.getGameRoomHeadCount(gameRoomId);
        byte newHeadCount = (byte)(currentHeadCount-1);
        stringByteRedisTemplate.opsForValue().set("gameRoom:"+gameRoomId, newHeadCount);
    }

    public byte addGameRoomHeadCount(Long gameRoomId) {
        byte currentHeadCount = this.getGameRoomHeadCount(gameRoomId);
        byte newHeadCount = (byte)(currentHeadCount+1);
        stringByteRedisTemplate.opsForValue().set("gameRoom:"+gameRoomId, newHeadCount);
        return newHeadCount;
    }

    public byte getGameRoomHeadCount(Long gameRoomId) {
        return Objects.requireNonNull(stringByteRedisTemplate.opsForValue().get("gameRoom:" + gameRoomId));
    }

}