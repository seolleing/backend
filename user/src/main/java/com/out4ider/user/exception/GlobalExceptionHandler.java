package com.out4ider.user.exception;

import com.out4ider.user.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleRuntimeException(CustomException ex) {
        ExceptionDto body = new ExceptionDto(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(ex.getHttpStatus(), body);
    }

    private ResponseEntity<?> handleExceptionInternal(HttpStatus httpStatus,
                                                      ExceptionDto body) {
        return ResponseUtil.onFailed(httpStatus, body);
    }
}
