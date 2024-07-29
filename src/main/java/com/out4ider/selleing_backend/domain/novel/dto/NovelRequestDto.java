package com.out4ider.selleing_backend.domain.novel.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NovelRequestDto {
    private String title;
    private String startSentence;
    private List<NovelInfoRequestDto> novelInfoRequestDtos;
}
