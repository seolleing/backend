package com.out4ider.selleing_backend.domain.novel.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NovelResponseDto {
    private Long novelId;
    private String title;
    private String startSentence;
//    private Long likeCount
}
