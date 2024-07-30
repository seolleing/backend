package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoRequestDto;

import java.util.List;

public interface CustomNovelInfoRepository {
    public void batchInsert(List<NovelInfoRequestDto> novelInfoRequestDtos, Long novelId);
}
