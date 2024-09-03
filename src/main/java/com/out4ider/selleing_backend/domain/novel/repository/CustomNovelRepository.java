package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomNovelRepository {
    List<NovelEntity> findAllWithBookmark(Pageable pageable, @Param("userId") Long userId);

    List<NovelResponseDto> findAllOrderByNovelId(Long lastId);

    List<NovelResponseDto> findAllOrderByLikeCount(Integer likeCount,Long lastId);

}
