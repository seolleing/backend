package com.out4ider.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ExceptionDto {
    private int code;
    private String message;

}
