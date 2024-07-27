package com.out4ider.selleing_backend.global.security;

import com.out4ider.selleing_backend.domain.user.dto.UserResponseDto;
import lombok.Getter;

public class CustomUserDetails extends SimpleCustomUserDetails {

    private final String password;
    @Getter
    private final UserResponseDto userResponseDto;;

    public CustomUserDetails(String email, String role, String password, UserResponseDto userResponseDto) {
        super(email, role);
        this.password = password;
        this.userResponseDto = userResponseDto;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
