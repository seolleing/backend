package com.out4ider.selleing_backend.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionBody {
    private int code;
    private String message;
}
