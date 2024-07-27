package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.entity.NovelInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JPANovelInfoRepository extends JpaRepository<NovelInfoEntity,Long> {
    @Query("select n from NovelInfoEntity n where n.novel.novelId=?1")
    List<NovelInfoEntity> findByNovelId(Long novelId);
}
