package com.out4ider.selleing_backend.domain.novel.dto;

import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class NovelTotalResponseDto {
    private boolean isLiked;
    private boolean isBookmarked;
    private int likeCount;
    private List<NovelInfoResponseDto> novelInfoResponseDtos;
    private List<CommentResponseDto> commentResponseDtos;
}
