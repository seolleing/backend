package com.out4ider.selleing_backend.domain.bookmark.repository;

import com.out4ider.selleing_backend.domain.bookmark.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    void deleteByNovel_NovelIdAndUser_UserId(Long novelId, Long userId);
}
