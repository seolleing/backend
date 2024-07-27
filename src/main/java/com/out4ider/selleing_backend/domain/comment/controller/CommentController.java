package com.out4ider.selleing_backend.domain.comment.controller;

import com.out4ider.selleing_backend.domain.comment.dto.CommentRequestDto;
import com.out4ider.selleing_backend.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> saveComment(@RequestBody CommentRequestDto commentRequestDto, Principal principal) {
        Long commentId = commentService.save(commentRequestDto, principal.getName());
        return ResponseEntity.ok().body(commentId);
    }

//    @PutMapping("/{commentId}")
//    public ResponseEntity<?> updateComment(@PathVariable Long commentId) {}
}
