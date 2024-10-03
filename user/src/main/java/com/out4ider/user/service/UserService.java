package com.out4ider.user.service;

import com.out4ider.user.dto.LoginResponseDto;
import com.out4ider.user.dto.UserRequestDto;
import com.out4ider.user.dto.UserResponseDto;
import com.out4ider.user.entity.UserEntity;
import com.out4ider.user.exception.kind.NotFoundElementException;
import com.out4ider.user.exception.kind.NotMatchedException;
import com.out4ider.user.repository.UserRepository;
import com.out4ider.user.util.JWTUtil;
import com.out4ider.user.util.PasswordEncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void save(UserRequestDto userRequestDto) {
        String encryptedPassword = PasswordEncryptionUtil
                .encryptPassword(userRequestDto.getPassword());
        UserEntity userEntity = UserEntity.builder()
                .email(userRequestDto.getEmail())
                .encryptedPassword(encryptedPassword)
                .role("ROLE_USER").nickname("닉네임")
                .build();
        userRepository.save(userEntity);
    }

    public LoginResponseDto login(UserRequestDto userRequestDto) {
        UserEntity user = userRepository.findByEmail(userRequestDto.getEmail())
                .orElseThrow(() -> new NotFoundElementException("not found in user database"));
        if (!PasswordEncryptionUtil.checkPassword(
                userRequestDto.getPassword(),user.getEncryptedPassword())){
            throw new NotMatchedException("unmatched password");
        }
        List<Pair<String, String>> tokens = JWTUtil.generateTokens(user.getId(), user.getRole());
        UserResponseDto userResponseDto = new UserResponseDto(user.getId(), user.getNickname());
        return new LoginResponseDto(tokens, userResponseDto);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void update(Long userId, String nickname) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundElementException("not found in user database"));
        userEntity.setNickname(nickname);
        userRepository.save(userEntity);
    }
}
