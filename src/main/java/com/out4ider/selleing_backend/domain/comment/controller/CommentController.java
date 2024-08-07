package com.out4ider.selleing_backend.domain.comment.controller;

import com.out4ider.selleing_backend.domain.comment.dto.CommentRequestDto;
import com.out4ider.selleing_backend.domain.comment.service.CommentService;
import com.out4ider.selleing_backend.global.security.SimpleCustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> saveComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails) {
        Long commentId = commentService.save(commentRequestDto, simpleCustomUserDetails.getUserId());
        return ResponseEntity.ok().body(commentId);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable(name = "commentId") Long commentId, @RequestParam(name = "content") String content, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails) {
        commentService.update(commentId, content, simpleCustomUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "commentId") Long commentId, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails) {
        commentService.delete(commentId, simpleCustomUserDetails.getUserId());
        return ResponseEntity.ok().build();
    }

}
