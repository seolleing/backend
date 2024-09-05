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

    @PostMapping("/novel/{novelId}")
    public ResponseEntity<?> likeNovel(@PathVariable(name = "novelId") Long id,
                                       @AuthenticationPrincipal SimpleCustomUserDetails userDetails) {
        likeService.saveLikeNovel(id, userDetails.getUserId());
        return ResponseDto.onSuccess();
    }

    @PostMapping("/comment/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable(name = "commentId") Long id,
                                         @AuthenticationPrincipal SimpleCustomUserDetails userDetails) {
        likeService.saveLikeComment(id, userDetails.getUserId());
        return ResponseDto.onSuccess();
    }
}
