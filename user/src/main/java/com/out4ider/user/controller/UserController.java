package com.out4ider.user.controller;

import com.out4ider.user.dto.LoginResponseDto;
import com.out4ider.user.util.ResponseUtil;
import com.out4ider.user.dto.UserRequestDto;
import com.out4ider.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody UserRequestDto userRequestDto) {
        userService.save(userRequestDto);
        return ResponseUtil.onSuccess();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
        LoginResponseDto responseDto = userService.login(userRequestDto);
        return ResponseUtil.onSuccess(responseDto.getTokens(), responseDto.getBody());
    }

    @GetMapping
    public ResponseEntity<?> checkEmail(@RequestParam(name = "email") String email) {
        boolean checkEmail = userService.checkEmail(email);
        return ResponseUtil.onSuccess(checkEmail);
    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@RequestParam(name = "nickname") String nickname,
                                        @RequestHeader(name = "X-User-Id") Long userId) {
        userService.update(userId, nickname);
        return ResponseUtil.onSuccess(nickname);
    }

    //ERD 완성되고 짜기
    /*@DeleteMapping
    public ResponseEntity<?> deleteUser() {
        return null;
    }*/
}
