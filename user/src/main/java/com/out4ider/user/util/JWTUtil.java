package com.out4ider.user.util;

import com.out4ider.user.redis.RedisKeyPrefix;
import com.out4ider.user.redis.RedisService;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {
    private final SecretKey secretKey;
    private final RedisService redisService;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret, RedisService redisService) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.redisService = redisService;
    }

    private String createToken(String category, Long userId, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category).claim("userId", userId)
                .claim("role", role).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(secretKey)
                .compact();
    }

    public List<Pair<String, String>> generateTokens(Long userId, String role) {
        long accessExpiredMs = 600000L;
        long refreshExpiredMs = 86400000L;
        List<Pair<String, String>> tokens = new ArrayList<>();
        tokens.add(Pair.of("Authorization", createToken("access", userId, role, accessExpiredMs)));
        String refresh = createToken("refresh", userId, role, refreshExpiredMs);
        redisService.setValue(RedisKeyPrefix.REFRESH, userId, refresh, refreshExpiredMs);
        tokens.add(Pair.of("Refresh", refresh));
        return tokens;
    }
}
