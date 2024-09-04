package com.out4ider.selleing_backend.global.common.dto;

import com.out4ider.selleing_backend.global.exception.ExceptionBody;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseDto {
    public static ResponseEntity<?> onSuccess() {
        return ResponseEntity.ok().build();
    }

    public static <T> ResponseEntity<T> onSuccess(T body) {
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<ExceptionBody> onFailed(
            HttpStatusCode httpStatusCode, ExceptionBody body) {
        return ResponseEntity.status(httpStatusCode).body(body);
    }

}
