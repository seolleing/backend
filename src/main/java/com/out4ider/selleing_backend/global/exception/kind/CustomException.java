package com.out4ider.selleing_backend.global.exception.kind;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CustomException extends RuntimeException{
    int code;
    String message;
    HttpStatusCode statusCode;
    public CustomException(String message) {
        super(message);
    }
}
