package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoResponseDto;

import java.util.List;

public interface CustomNovelInfoRepository {
    void batchInsert(List<NovelInfoRequestDto> novelInfoRequestDtos, Long novelId);

    List<NovelInfoResponseDto> findByNovelId(Long novelId);

}
