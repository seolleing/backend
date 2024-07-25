package com.out4ider.selleing_backend.global.security;

import lombok.Getter;

public class CustomUserDetails extends SimpleCustomUserDetails {

    private final String password;
    @Getter
    private final String nickname;

    public CustomUserDetails(String email, String role, String password, String nickname) {
        super(email, role);
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

}
