package com.out4ider.selleing_backend.domain.like.controller;

import com.out4ider.selleing_backend.domain.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/novels/{novelId}")
    public ResponseEntity<?> likeNovel(@PathVariable(name = "novelId") Long id, Principal principal){
        int likeCount = likeService.likeNovel(id, principal.getName());
        return ResponseEntity.ok().body(likeCount);
    }

    @DeleteMapping("/novels/{novelId}")
    public ResponseEntity<?> unlikeNovel(@PathVariable(name = "novelId") Long id, Principal principal){
        int likeCount = likeService.unlikeNovel(id, principal.getName());
        return ResponseEntity.ok().body(likeCount);
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<?> likeComment(@PathVariable(name = "commentId") Long id, Principal principal){
        int likeCount = likeService.likeComment(id, principal.getName());
        return ResponseEntity.ok().body(likeCount);
    }
}
