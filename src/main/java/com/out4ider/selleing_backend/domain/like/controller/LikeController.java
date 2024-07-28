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

    @PostMapping("/{id}")
    public ResponseEntity<?> likeIt(@PathVariable(name = "id") Long id, @RequestParam(name = "type") String type, Principal principal){
        int likeCount = likeService.like(id, type, principal.getName());
        return ResponseEntity.ok().body(likeCount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> unlikeIt(@PathVariable(name = "id") Long id, @RequestParam(name = "type") String type, Principal principal){
        int likeCount = likeService.unlike(id, type, principal.getName());
        return ResponseEntity.ok().body(likeCount);
    }
}
