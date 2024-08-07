package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeNovelRepository extends JpaRepository<LikeNovelEntity, Long>, CustomLikeNovelRepository {
    @Query("select l from LikeNovelEntity l join fetch l.user where l.novel.novelId=?1")
    List<LikeNovelEntity> findLikeNovel(Long novelId);

    @Query("select l from LikeNovelEntity l where l.novel.novelId=?1 and l.user.userId=?1")
    Optional<LikeNovelEntity> findLikeNovelWithNovelIdAndUserId(Long novelId, Long userId);
}
