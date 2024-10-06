package com.out4ider.gateway.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.out4ider.gateway.exception.CustomException;
import com.out4ider.gateway.exception.ExceptionDto;
import com.out4ider.gateway.exception.kind.ExpiredTokenException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class CustomExceptionHandlingFilter implements WebFilter {
    private final ObjectMapper objectMapper;

    public CustomExceptionHandlingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(CustomException.class, ex -> {
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(ex.getHttpStatus());
                    ExceptionDto exceptionDto = new ExceptionDto(ex.getCode(), ex.getMessage());
                    try {
                        String jsonResponse = objectMapper.writeValueAsString(exceptionDto);
                        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
                        DataBuffer buffer = dataBufferFactory.wrap(jsonResponse.getBytes(StandardCharsets.UTF_8));
                        return response.writeWith(Mono.just(buffer));
                    } catch (Exception exception) {
                        return Mono.error(exception);
                    }
                });
    }
}
