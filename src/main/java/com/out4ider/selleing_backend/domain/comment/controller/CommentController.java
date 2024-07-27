package com.out4ider.selleing_backend.domain.comment.controller;

import com.out4ider.selleing_backend.domain.comment.dto.CommentRequestDto;
import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import com.out4ider.selleing_backend.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> saveComment(@RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.save(commentRequestDto);
        return ResponseEntity.ok().body(commentResponseDto);
    }

//    @PutMapping("/{commentId}")
//    public ResponseEntity<?> updateComment(@PathVariable Long commentId) {}
}
