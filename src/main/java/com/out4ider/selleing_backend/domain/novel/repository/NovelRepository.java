package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NovelRepository extends JpaRepository<NovelEntity, Long> {
    @Query("select n from NovelEntity n")
    List<NovelEntity> findAllWithNovelId(Pageable pageable);

    @Query("select n from NovelEntity n left join n.likeNovels l group by n order by count(l) desc")
    List<NovelEntity> findAllWithLikeNovel(Pageable pageable);

    @Query("select n from NovelEntity n join n.bookmarks b where b.user.userId=:userId")
    List<NovelEntity> findAllWithBookmark(Pageable pageable, @Param("userId") Long userId);
}
