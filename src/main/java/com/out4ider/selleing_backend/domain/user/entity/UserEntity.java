package com.out4ider.selleing_backend.domain.user.entity;

import com.out4ider.selleing_backend.domain.user.dto.UserResponseDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name="password")
    private String encryptedPassword;

    @Column(name = "nickname")
    @Setter
    private String nickname;

    @Column(name = "role")
    private String role;

    public UserResponseDto toUserResponseDto() {
        return new UserResponseDto(this.userId, this.getNickname());
    }
}
