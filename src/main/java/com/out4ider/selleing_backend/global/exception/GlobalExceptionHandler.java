package com.out4ider.selleing_backend.global.exception;

import com.out4ider.selleing_backend.global.common.dto.ResponseDto;
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
    public ResponseEntity<ExceptionBody> handleRuntimeException(RuntimeException ex) {
        ExceptionBody body = new ExceptionBody(ExceptionEnum.UNHANDLED.ordinal(), ex.getMessage());
        return this.handleExceptionInternal(body, HttpStatus.LOCKED);
    }

    @ExceptionHandler(NotFoundElementException.class)
    public ResponseEntity<ExceptionBody> handleNotFoundElementException(NotFoundElementException ex) {
        ExceptionBody body = new ExceptionBody(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ExceptionBody> handleNotAuthorizedException(NotAuthorizedException ex) {
        ExceptionBody body = new ExceptionBody(ex.getCode(), ex.getMessage());
        return this.handleExceptionInternal(body, ex.getStatusCode());
    }

    private ResponseEntity<ExceptionBody> handleExceptionInternal(ExceptionBody body, HttpStatusCode statusCode) {
        return ResponseDto.onFailed(statusCode, body);
    }
}
