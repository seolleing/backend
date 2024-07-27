package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NovelRepository extends JpaRepository<NovelEntity, Long> {
    Page<NovelEntity> findAll(Pageable pageable);
}
