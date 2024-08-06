package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeNovelRepository extends JpaRepository<LikeNovelEntity, Long>, CustomLikeNovelRepository {
    @Query("select l from LikeNovelEntity l join fetch l.user where l.novel.novelId=?1")
    List<LikeNovelEntity> findLikeNovel(Long novelId);
}
