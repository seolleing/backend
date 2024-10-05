package com.out4ider.gateway.security;

import com.out4ider.gateway.redis.RedisKeyPrefix;
import com.out4ider.gateway.redis.RedisService;
import com.out4ider.gateway.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class ReissueFilter implements WebFilter {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    public ReissueFilter(JWTUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Boolean reissue = exchange.getAttribute("reissue");
        if (reissue!=null && reissue) {
            String refresh = exchange.getRequest().getHeaders().getFirst("Refresh");
            if (refresh == null) {
                throw new RuntimeException("This is Invalid Token");
            }
            try {
                jwtUtil.isExpired(refresh);
            } catch (ExpiredJwtException e) {
                throw new RuntimeException("This is Invalid Token");
            }
            String category = jwtUtil.getCategory(refresh);
            if (!category.equals("refresh")) {
                throw new RuntimeException("This is Invalid Token");

            }
            Long userId = jwtUtil.getUserId(refresh);
            String role = jwtUtil.getRole(refresh);
            if(!Objects.equals(redisService.getValue(RedisKeyPrefix.REFRESH,userId), refresh)){
                throw new RuntimeException("This is Invalid Token");
            }
            ServerHttpResponse response = exchange.getResponse();
            String newAccess = jwtUtil.createToken("access", userId, role, 600000L);
            response.getHeaders().add("Authorization", newAccess);
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        return chain.filter(exchange);
    }
}
