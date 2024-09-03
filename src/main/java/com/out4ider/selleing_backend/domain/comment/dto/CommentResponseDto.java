package com.out4ider.selleing_backend.domain.comment.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String nickname;
    private int likeCount;

    @QueryProjection
    public CommentResponseDto(Long commentId, String content, String nickname, int likeCount) {
        this.commentId = commentId;
        this.content = content;
        this.nickname = nickname;
        this.likeCount = likeCount;
    }
}
