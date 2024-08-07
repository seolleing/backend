package com.out4ider.selleing_backend.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Byte> stringByteRedisTemplate;
    private final RedisTemplate<String, Long> stringLongRedisTemplate;
    private final RedisTemplate<Long, String> longStringRedisTemplate;
    private final Duration expiredTime = Duration.ofHours(2);

    //common
    public boolean checkValueExisting(String keyName, Long userId) {
        return Boolean.TRUE.equals(stringLongRedisTemplate.opsForSet().isMember(keyName, userId));
    }

    //jwt
    public void setToken(Long userId, String token, long expiredTime){
        longStringRedisTemplate.opsForValue().set(userId, token);
        longStringRedisTemplate.expire(userId, Duration.ofMillis(expiredTime));
    }

    public void deleteToken(Long userId){
        longStringRedisTemplate.delete(userId);
    }

    public String getToken(Long userId){
        return longStringRedisTemplate.opsForValue().get(userId);
    }

    //schedule redis service
    public void removeAllKey(Set<String> keyNames) {
        stringLongRedisTemplate.delete(keyNames);
    }

    public Set<String> getAllKey(String keyName) {
        return stringLongRedisTemplate.keys(keyName);
    }

    public Set<Long> getAllValue(String keyName) {
        return stringLongRedisTemplate.opsForSet().members(keyName);
    }

    //like redis service
    public void removeValue(String keyName, Long userId) {
        stringLongRedisTemplate.opsForSet().remove(keyName, userId);
    }

    public void addValue(String keyName, Long userId) {
        stringLongRedisTemplate.opsForSet().add(keyName, userId);
    }


    //novel redis service
    public int getSize(String keyName) {
        Long newLikeNovelSize = stringLongRedisTemplate.opsForSet().size(keyName);
        return newLikeNovelSize == null ? 0 : newLikeNovelSize.intValue();
    }

    public boolean alreadyHasOldLikeNovelKey(Long novelId) {
        return Boolean.TRUE.equals(stringLongRedisTemplate.hasKey("oldLikeNovel:" + novelId));
    }

    @Async
    public void addOldLikeNovel(Long novelId, Long[] userIds) {
        String keyName = "oldLikeNovel:" + novelId;
        stringLongRedisTemplate.opsForSet().add(keyName, userIds);
        stringLongRedisTemplate.expire(keyName, expiredTime);
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
        byte newHeadCount = (byte) (currentHeadCount - 1);
        stringByteRedisTemplate.opsForValue().set("gameRoom:" + gameRoomId, newHeadCount);
    }

    public byte addGameRoomHeadCount(Long gameRoomId) {
        byte currentHeadCount = this.getGameRoomHeadCount(gameRoomId);
        byte newHeadCount = (byte) (currentHeadCount + 1);
        stringByteRedisTemplate.opsForValue().set("gameRoom:" + gameRoomId, newHeadCount);
        return newHeadCount;
    }

    public byte getGameRoomHeadCount(Long gameRoomId) {
        return Objects.requireNonNull(stringByteRedisTemplate.opsForValue().get("gameRoom:" + gameRoomId));
    }

}