package com.out4ider.selleing_backend.domain.novel.repository.novelinfo;

import com.out4ider.selleing_backend.domain.novel.entity.NovelInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NovelInfoRepository
        extends JpaRepository<NovelInfoEntity, Long>, CustomNovelInfoRepository {
}
