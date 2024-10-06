package com.out4ider.gateway.exception.kind;

import com.out4ider.gateway.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ExpiredTokenException extends CustomException {
    public ExpiredTokenException(String message) {
        super(0, message, HttpStatus.GONE);
    }
}
