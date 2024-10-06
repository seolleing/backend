package com.out4ider.gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException{
    private final int code;
    private final HttpStatus httpStatus;
    public CustomException(int code, String message, HttpStatus httpStatus) {
        super(message);
        this.code=code;
        this.httpStatus=httpStatus;
    }
}
