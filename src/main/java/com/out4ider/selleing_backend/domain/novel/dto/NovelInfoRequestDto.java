package com.out4ider.selleing_backend.domain.novel.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NovelInfoRequestDto {
    private Long userId;
    private String content;
}
