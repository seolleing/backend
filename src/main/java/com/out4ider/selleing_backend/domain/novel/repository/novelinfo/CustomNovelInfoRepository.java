package com.out4ider.selleing_backend.domain.novel.repository.novelinfo;

import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoRequestDto;
import com.out4ider.selleing_backend.domain.novel.dto.NovelInfoResponseDto;

import java.util.List;

public interface CustomNovelInfoRepository {
    void batchInsert(Long novelId, List<NovelInfoRequestDto> novelInfoRequestDtos);

    List<NovelInfoResponseDto> findByNovelIdWithUser(Long novelId);

}
