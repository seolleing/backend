package com.out4ider.selleing_backend.global.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<Long, String> longStringRedisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    //jwt
    public void setToken(Long userId, String token, long expiredTime) {
        longStringRedisTemplate.opsForValue().set(userId, token);
        longStringRedisTemplate.expire(userId, Duration.ofMillis(expiredTime));
    }

    public void deleteToken(Long userId) {
        longStringRedisTemplate.delete(userId);
    }

    public String getToken(Long userId) {
        return longStringRedisTemplate.opsForValue().get(userId);
    }

    //gameRoom
    public void setGameRoomInitialHeadCount(Long gameRoomId) {
        stringRedisTemplate.opsForValue().set("gameRoom:" + gameRoomId, "1");
    }

    public void deleteGameRoomHeadCount(Long gameRoomId) {
        stringRedisTemplate.delete("gameRoom:" + gameRoomId);
    }

    public void subGameRoomHeadCount(Long gameRoomId) {
        stringRedisTemplate.opsForValue().decrement("gameRoom:" + gameRoomId);
    }

    public Long addGameRoomHeadCount(Long gameRoomId) {
        return stringRedisTemplate.opsForValue().increment("gameRoom:" + gameRoomId);
    }

    public String getGameRoomHeadCount(Long gameRoomId) {
        return Objects.requireNonNull(stringRedisTemplate.opsForValue().get("gameRoom:" + gameRoomId));
    }

}