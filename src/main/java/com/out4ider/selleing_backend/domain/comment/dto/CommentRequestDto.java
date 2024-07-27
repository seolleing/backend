package com.out4ider.selleing_backend.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private Long novelId;
    private String content;
}
