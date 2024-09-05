package com.out4ider.selleing_backend.domain.novel.repository.novel;

import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;

import java.util.List;

public interface CustomNovelRepository {
    Integer findLikeCountByNovelId(Long novelId);

    List<NovelResponseDto> findOrderByNovelId(Long lastId);

    List<NovelResponseDto> findOrderByNovelIdInBookmark(Long lastId, Long userId);

    List<NovelResponseDto> findOrderByLikeCount(Long lastId, Integer likeCount);
}