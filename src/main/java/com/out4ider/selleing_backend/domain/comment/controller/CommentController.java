package com.out4ider.selleing_backend.domain.comment.controller;

import com.out4ider.selleing_backend.domain.comment.dto.CommentRequestDto;
import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import com.out4ider.selleing_backend.domain.comment.service.CommentService;
import com.out4ider.selleing_backend.global.common.dto.ResponseDto;
import com.out4ider.selleing_backend.global.security.SimpleCustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> saveComment(@RequestBody CommentRequestDto commentRequestDto,
                                         @AuthenticationPrincipal SimpleCustomUserDetails userDetails) {
        Long commentId = commentService.save(commentRequestDto, userDetails.getUserId());
        return ResponseDto.onSuccess(commentId);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable(name = "commentId") Long commentId,
                                           @RequestParam(name = "content") String content,
                                           @AuthenticationPrincipal SimpleCustomUserDetails userDetails) {
        commentService.update(commentId, content, userDetails.getUserId());
        return ResponseDto.onSuccess();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "commentId") Long commentId,
                                           @AuthenticationPrincipal SimpleCustomUserDetails userDetails) {
        commentService.delete(commentId, userDetails.getUserId());
        return ResponseDto.onSuccess();
    }

    @GetMapping("/{novelId}")
    public ResponseEntity<?> getComments(@PathVariable(name = "novelId") Long novelId,
                                         @RequestParam(name = "lastId") Long lastId) {
        List<CommentResponseDto> commentResponseDtos = commentService.findMore(novelId, lastId);
        return ResponseDto.onSuccess(commentResponseDtos);
    }
}
