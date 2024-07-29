package com.out4ider.selleing_backend.global.exception.kind;

import org.springframework.http.HttpStatusCode;

public class AlreadyDoingException extends CustomException{
    public AlreadyDoingException(int code, String message, HttpStatusCode statusCode) {
        super(message);
        super.code = code;
        super.message = message;
        super.statusCode = statusCode;
    }
}
