package com.out4ider.selleing_backend.domain.novel.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class NovelResponseDto {
    private Long novelId;
    private String title;
    private String startSentence;
    private int likeCount;

    @QueryProjection
    public NovelResponseDto(Long novelId, String title, String startSentence, int likeCount) {
        this.novelId = novelId;
        this.title = title;
        this.startSentence = startSentence;
        this.likeCount = likeCount;
    }
}
