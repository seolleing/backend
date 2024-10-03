package com.out4ider.user.exception.kind;

import com.out4ider.user.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotMatchedException extends CustomException {

    public NotMatchedException(String message) {
        super(4, message, HttpStatus.UNAUTHORIZED);
    }
}
