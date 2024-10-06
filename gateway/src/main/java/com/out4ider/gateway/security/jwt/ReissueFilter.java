package com.out4ider.gateway.security.jwt;

import com.out4ider.gateway.exception.kind.ExpiredTokenException;
import com.out4ider.gateway.exception.kind.InvalidTokenException;
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
                return Mono.error(new InvalidTokenException("refresh token null"));
            }
            try {
                jwtUtil.isExpired(refresh);
            } catch (ExpiredJwtException e) {
                return Mono.error(new ExpiredTokenException("refresh token expired"));
            }
            String category = jwtUtil.getCategory(refresh);
            if (!category.equals("refresh")) {
                return Mono.error(new InvalidTokenException("unmatched refresh category"));

            }
            Long userId = jwtUtil.getUserId(refresh);
            String role = jwtUtil.getRole(refresh);
            if(!Objects.equals(redisService.getValue(RedisKeyPrefix.REFRESH,userId), refresh)){
                throw new InvalidTokenException("unmatched refresh in redis");
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
