package com.out4ider.selleing_backend.domain.novel.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class NovelInfoResponseDto {
    private String nickname;
    private String content;

    @QueryProjection
    public NovelInfoResponseDto(String nickname, String content) {
        this.nickname = nickname;
        this.content = content;
    }
}
