package com.out4ider.selleing_backend.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.out4ider.selleing_backend.global.exception.ExceptionBody;
import com.out4ider.selleing_backend.global.exception.kind.AccessTokenExpiredException;
import com.out4ider.selleing_backend.global.exception.kind.CustomException;
import com.out4ider.selleing_backend.global.exception.kind.FailedLoginException;
import com.out4ider.selleing_backend.global.exception.kind.InvalidLoginTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandleFilter extends OncePerRequestFilter {
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessTokenExpiredException | InvalidLoginTokenException | FailedLoginException exception) {
            setExceptionResponse(response, exception);
        }
    }

    private void setExceptionResponse(HttpServletResponse response, CustomException exception) throws IOException {
        response.setStatus(exception.getStatusCode().value());
        ExceptionBody exceptionBody = new ExceptionBody(exception.getCode(), exception.getMessage());
        String jsonResponse = mapper.writeValueAsString(exceptionBody);
        response.getWriter().write(jsonResponse);
    }
}
