package com.out4ider.user.exception.kind;

import com.out4ider.user.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundElementException extends CustomException {

    public NotFoundElementException(String message) {
        super(2, message, HttpStatus.NOT_FOUND);
    }
}
