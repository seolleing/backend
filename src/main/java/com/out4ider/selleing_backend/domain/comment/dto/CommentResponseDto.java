package com.out4ider.selleing_backend.domain.comment.dto;

import lombok.*;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponseDto {
    private Long commentId;
    private String content;
//    private String nickname;
}
