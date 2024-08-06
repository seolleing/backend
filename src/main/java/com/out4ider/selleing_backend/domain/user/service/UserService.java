package com.out4ider.selleing_backend.domain.user.service;

import com.out4ider.selleing_backend.domain.user.dto.UserRequestDto;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import com.out4ider.selleing_backend.domain.user.repository.UserRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void save(UserRequestDto userRequestDto) {
        String encryptedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        UserEntity userEntity = UserEntity.builder()
                .email(userRequestDto.getEmail())
                .encryptedPassword(encryptedPassword)
                .role("ROLE_USER")
                .nickname("닉네임")
                .build();
        userRepository.save(userEntity);
    }

    public boolean checkEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void update(Long userId, String nickname) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        userEntity.setNickname(nickname);
        userRepository.save(userEntity);
    }


}
