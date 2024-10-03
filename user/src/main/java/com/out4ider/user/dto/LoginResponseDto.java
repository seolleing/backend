package com.out4ider.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private List<Pair<String, String>> tokens;
    private UserResponseDto body;
}
