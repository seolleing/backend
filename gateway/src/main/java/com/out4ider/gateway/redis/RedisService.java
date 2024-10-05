package com.out4ider.gateway.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public String getValue(RedisKeyPrefix prefix, Long userId) {
        return stringRedisTemplate.opsForValue().get(prefix.getDescription()+userId);
    }

    public void deleteKey(RedisKeyPrefix prefix, Long userId) {
        stringRedisTemplate.delete(prefix.getDescription()+userId);
    }
}