package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NovelRepository extends JpaRepository<NovelEntity, Long> {
    @Query("select distinct n from NovelEntity n left join fetch n.likeNovels")
    Page<NovelEntity> findAll(Pageable pageable);
    @Query("select distinct n from NovelEntity n left join fetch n.likeNovels where n.novelId=?1")
    Optional<NovelEntity> findByIdWithLikeNovel(Long id);
}
