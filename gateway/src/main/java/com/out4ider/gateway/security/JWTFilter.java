package com.out4ider.gateway.security;

import com.out4ider.gateway.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class JWTFilter implements WebFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String accessToken = request.getHeaders().getFirst("Authorization");
        if (ObjectUtils.isEmpty(accessToken)) {
            return chain.filter(exchange);
        }
        String path = request.getURI().getPath();
        HttpMethod httpMethod = request.getMethod();
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            if (path.equals("/reissue") && httpMethod == HttpMethod.POST) {
                exchange.getAttributes().put("reissue", true);
                return chain.filter(exchange);
            } else {
                throw new RuntimeException("access token expired");
            }
        }
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            throw new RuntimeException("This is Invalid Token");
        }
        Long userId = jwtUtil.getUserId(accessToken);
        String role = jwtUtil.getRole(accessToken);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userId, null, roles);
        SecurityContext securityContext = new SecurityContextImpl(authenticationToken);
        if (checkNeedUserIdPath(path, httpMethod)) {
            request.mutate().header("X-User-Id", String.valueOf(userId));
        }
        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder
                .withSecurityContext(Mono.just(securityContext)));
    }

    private boolean checkNeedUserIdPath(String path, HttpMethod method) {
        return path.equals("/api/users") && method.equals(HttpMethod.PATCH);
    }
}
