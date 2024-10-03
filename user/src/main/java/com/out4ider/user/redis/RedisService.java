package com.out4ider.user.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate stringRedisTemplate;

    public void setValue(RedisKeyPrefix prefix, Long userId,
                         String token, long expiredTime) {
        stringRedisTemplate.opsForValue().set(prefix.getDescription() + userId,
                token, Duration.ofSeconds(expiredTime));
    }
}