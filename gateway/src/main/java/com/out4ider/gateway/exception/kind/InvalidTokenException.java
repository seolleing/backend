package com.out4ider.gateway.exception.kind;

import com.out4ider.gateway.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends CustomException {
    public InvalidTokenException(String message) {
        super(1, message, HttpStatus.LOCKED);
    }
}
