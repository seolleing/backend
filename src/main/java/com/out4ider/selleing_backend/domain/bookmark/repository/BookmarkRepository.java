package com.out4ider.selleing_backend.domain.bookmark.repository;

import com.out4ider.selleing_backend.domain.bookmark.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    @Modifying
    @Query("delete from BookmarkEntity b where b.novel.novelId=?1 and b.user.userId=?2")
    void deleteBookmarkByNovelIdAndUserId(Long novelId, Long userId);
}
