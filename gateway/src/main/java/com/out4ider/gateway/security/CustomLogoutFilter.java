package com.out4ider.gateway.security;

import com.out4ider.gateway.redis.RedisKeyPrefix;
import com.out4ider.gateway.redis.RedisService;
import com.out4ider.gateway.util.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CustomLogoutFilter implements WebFilter{
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    public CustomLogoutFilter(JWTUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod httpMethod = request.getMethod();
        if(path.equals("/logout")&&httpMethod==HttpMethod.POST){
            String refresh = request.getHeaders().getFirst("Refresh");
            if(!ObjectUtils.isEmpty(refresh)){
                Long userId = jwtUtil.getUserId(refresh);
                redisService.deleteKey(RedisKeyPrefix.REFRESH, userId);
            }
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            response.setStatusCode(HttpStatus.OK);
            headers.set("Authorization", null);
            headers.set("Refresh", null);
            return Mono.empty();
        }
        return chain.filter(exchange);
    }
}
