package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeNovelRepository extends JpaRepository<LikeNovelEntity, Long> {
    @Modifying
    @Query("delete from LikeNovelEntity l where l.novel.novelId=?1 and l.user.email=?2")
    void deleteByNovelIdAndEmail(Long novelId, String email);
}