package com.out4ider.selleing_backend.global.exception.kind;

import org.springframework.http.HttpStatusCode;

public class NotAuthorizedException extends CustomException{
    public NotAuthorizedException(int code, String message, HttpStatusCode statusCode) {
        super(message);
        super.code = code;
        super.message = message;
        super.statusCode = statusCode;
    }
}
