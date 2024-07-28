package com.out4ider.selleing_backend.domain.comment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String nickname;
    private int likeCount;
}
