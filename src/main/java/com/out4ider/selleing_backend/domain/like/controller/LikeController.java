package com.out4ider.selleing_backend.domain.like.controller;

import com.out4ider.selleing_backend.domain.like.service.LikeService;
import com.out4ider.selleing_backend.global.common.dto.ResponseDto;
import com.out4ider.selleing_backend.global.security.SimpleCustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/novels/{novelId}")
    public ResponseEntity<?> likeNovel(@PathVariable(name = "novelId") Long id, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails){
        likeService.likeNovel(id, simpleCustomUserDetails.getUserId());
        return ResponseDto.onSuccess();
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable(name = "commentId") Long id, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails){
        likeService.likeComment(id,simpleCustomUserDetails.getUserId());
        return ResponseDto.onSuccess();
    }
}
