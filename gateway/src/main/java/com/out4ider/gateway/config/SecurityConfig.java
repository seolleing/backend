package com.out4ider.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.out4ider.gateway.redis.RedisService;
import com.out4ider.gateway.security.custom.CustomAuthenticationEntryPoint;
import com.out4ider.gateway.security.custom.CustomExceptionHandlingFilter;
import com.out4ider.gateway.security.custom.CustomLogoutFilter;
import com.out4ider.gateway.security.jwt.JWTFilter;
import com.out4ider.gateway.security.jwt.ReissueFilter;
import com.out4ider.gateway.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        http
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        http
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable);
        http
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        http
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/users/test").permitAll()
                        .pathMatchers("/api/users/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/users").permitAll()
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/actuator/**").permitAll()
                        .anyExchange().authenticated());
        http
                .addFilterBefore(new JWTFilter(jwtUtil), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterBefore(new ReissueFilter(jwtUtil, redisService),
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisService),
                        SecurityWebFiltersOrder.LOGOUT);
        http
                .addFilterAfter(new CustomExceptionHandlingFilter(objectMapper),
                        SecurityWebFiltersOrder.LAST);
        http
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper)));
        return http.build();
    }
}
