package com.out4ider.selleing_backend.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestDto {
    private String email;
    private String password;
}
