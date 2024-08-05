package com.out4ider.selleing_backend.domain.user.controller;

import com.out4ider.selleing_backend.domain.user.dto.UserRequestDto;
import com.out4ider.selleing_backend.domain.user.service.UserService;
import com.out4ider.selleing_backend.global.security.SimpleCustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> checkEmail(@RequestParam(name = "email") String email) {
        boolean checkEmail = userService.checkEmail(email);
        return ResponseEntity.ok().body(checkEmail);
    }

    @PatchMapping
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails, @RequestParam(name = "nickname") String nickname) {
        userService.update(simpleCustomUserDetails.getUserId(), nickname);
        return ResponseEntity.ok().body(nickname);
    }
    
    //ERD 완성되고 짜기
    /*@DeleteMapping
    public ResponseEntity<?> deleteUser() {
        return null;
    }*/
}
