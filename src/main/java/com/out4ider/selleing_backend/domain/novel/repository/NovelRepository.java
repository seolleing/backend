package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NovelRepository extends JpaRepository<NovelEntity, Long> {
    @Query("select distinct n from NovelEntity n left join fetch n.likeNovels where n.novelId=?1")
    Optional<NovelEntity> findByIdWithLikeNovel(Long id);
    @Query("select n from NovelEntity n left join n.likeNovels l group by n order by count(l) desc")
    Page<NovelEntity> findAllWithLikeOrder(Pageable pageable);
    @Query("select n from NovelEntity n join n.likeNovels l join l.user u where u.email=:email")
    Page<NovelEntity> findAllWithLike(Pageable pageable, @Param("email") String email);
}
