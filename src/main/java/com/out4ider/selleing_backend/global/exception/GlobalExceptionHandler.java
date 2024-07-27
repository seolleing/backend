package com.out4ider.selleing_backend.global.exception;

import com.out4ider.selleing_backend.global.exception.kind.NotAuthorizedException;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException ex) {
        ExceptionResponse body = new ExceptionResponse(ExceptionEnum.UNHANDLED.ordinal(), ex.getMessage());
        return this.handleExceptionInternal(body, HttpStatus.LOCKED);
    }

    @ExceptionHandler(NotFoundElementException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundElementException(NotFoundElementException ex) {
        ExceptionResponse body = new ExceptionResponse(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleNotAuthorizedException(NotAuthorizedException ex) {
        ExceptionResponse body = new ExceptionResponse(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    private ResponseEntity<ExceptionResponse> handleExceptionInternal(ExceptionResponse body, HttpStatusCode statusCode) {
        return ResponseEntity.status(statusCode)
                .body(body);
    }
}
