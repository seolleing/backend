package com.out4ider.selleing_backend.global.security;


import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.AccessTokenExpiredException;
import com.out4ider.selleing_backend.global.exception.kind.InvalidLoginTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader("Authorization");
        if (accessToken == null) {
            log.info("access token empty");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            String path = request.getServletPath();
            String method = request.getMethod();
            if (path.equals("/reissue") && method.equals("POST")) {
                request.setAttribute("reissue", true);
                filterChain.doFilter(request, response);
            }
            else{
                throw new AccessTokenExpiredException(ExceptionEnum.ACCESSTOKENEXPIRED.ordinal(), "Access token expired", HttpStatus.UNAUTHORIZED);
            }
            return;
        }

        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {
            throw new InvalidLoginTokenException(ExceptionEnum.INVALIDTOKEN.ordinal(), "This is Invalid Token", HttpStatus.UNAUTHORIZED);
        }

        String email = jwtUtil.getEmail(accessToken);
        String role = jwtUtil.getRole(accessToken);
        SimpleCustomUserDetails simpleCustomUserDetails = new SimpleCustomUserDetails(email, role);
        Authentication authentication = new UsernamePasswordAuthenticationToken(simpleCustomUserDetails, null, simpleCustomUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
