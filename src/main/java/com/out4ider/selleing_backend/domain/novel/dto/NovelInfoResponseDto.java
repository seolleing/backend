package com.out4ider.selleing_backend.domain.novel.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NovelInfoResponseDto {
    private String nickname;
    private String content;
}
