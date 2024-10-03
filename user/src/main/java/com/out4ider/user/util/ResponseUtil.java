package com.out4ider.user.util;

import com.out4ider.user.exception.ExceptionDto;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {
    public static ResponseEntity<?> onSuccess() {
        return ResponseEntity.ok().build();
    }

    public static <T> ResponseEntity<T> onSuccess(T body) {
        return ResponseEntity.ok(body);
    }

    public static <T> ResponseEntity<T> onSuccess(List<Pair<String,String>> headers, T body) {
        return ResponseEntity.ok().headers(createHttpHeaders(headers)).body(body);
    }

    public static ResponseEntity<?> onFailed(
            HttpStatus httpStatus, ExceptionDto body) {
        return ResponseEntity.status(httpStatus.value()).body(body);
    }

    private static HttpHeaders createHttpHeaders(List<Pair<String,String>> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(header -> httpHeaders.add(header.getKey(), header.getValue()));
        return httpHeaders;
    }
}
