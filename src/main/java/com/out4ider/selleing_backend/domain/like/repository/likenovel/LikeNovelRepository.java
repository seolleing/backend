package com.out4ider.selleing_backend.domain.like.repository.likenovel;

import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeNovelRepository extends JpaRepository<LikeNovelEntity, Long> {
    boolean existsByNovel_NovelIdAndUser_UserId(Long novelId, Long userId);
}
