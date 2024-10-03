package com.out4ider.user.util;

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
    private static SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    private static String createToken(String category, Long userId, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("category", category).claim("userId", userId)
                .claim("role", role).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs)).signWith(secretKey)
                .compact();
    }

    public static List<Pair<String, String>> generateTokens(Long userId, String role){
        List<Pair<String, String>> tokens = new ArrayList<>();
        tokens.add(Pair.of("Authorization", createToken("access", userId, role,600000L)));
        tokens.add(Pair.of("Refresh", createToken("refresh", userId, role, 86400000L)));
        return tokens;
    }
}
