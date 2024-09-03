package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeNovelRepository extends JpaRepository<LikeNovelEntity, Long>, CustomeLikeNovelRepository {
    Optional<LikeNovelEntity> findByNovel_NovelIdAndUser_UserId(Long novelId, Long userId);
}
