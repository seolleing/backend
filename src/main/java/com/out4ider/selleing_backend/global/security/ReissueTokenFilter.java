package com.out4ider.selleing_backend.global.security;

import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.InvalidLoginTokenException;
import com.out4ider.selleing_backend.global.common.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ReissueTokenFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    public ReissueTokenFilter(final JWTUtil jwtUtil, RedisService redisService) {
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Boolean reissue = (Boolean) request.getAttribute("reissue");
        if (reissue!=null && reissue) {
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("Refresh")) {
                    String refresh = cookie.getValue();
                    if (refresh == null) {
                        throw new InvalidLoginTokenException(ExceptionEnum.INVALIDTOKEN.ordinal(), "This is Invalid Token", HttpStatus.UNAUTHORIZED);
                    }
                    try {
                        jwtUtil.isExpired(refresh);
                    } catch (ExpiredJwtException e) {
                        throw new InvalidLoginTokenException(ExceptionEnum.INVALIDTOKEN.ordinal(), "This is Invalid Token", HttpStatus.UNAUTHORIZED);
                    }

                    String category = jwtUtil.getCategory(refresh);

                    if (!category.equals("refresh")) {

                        throw new InvalidLoginTokenException(ExceptionEnum.INVALIDTOKEN.ordinal(), "This is Invalid Token", HttpStatus.UNAUTHORIZED);
                    }

                    Long userId = jwtUtil.getUserId(refresh);
                    String role = jwtUtil.getRole(refresh);

                    if(Objects.equals(redisService.getToken(userId), refresh)){
                        throw new InvalidLoginTokenException(ExceptionEnum.INVALIDTOKEN.ordinal(), "This is Invalid Token", HttpStatus.UNAUTHORIZED);
                    }

                    String newAccess = jwtUtil.createToken("access", userId, role, 600000L);
                    response.setHeader("Authorization", newAccess);
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
