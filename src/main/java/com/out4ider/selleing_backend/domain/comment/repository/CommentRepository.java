package com.out4ider.selleing_backend.domain.comment.repository;

import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("select c from CommentEntity c join fetch c.user where c.id=?1")
    Optional<CommentEntity> findByIdWithUser(Long id);

    @Query("select c from CommentEntity c join fetch c.user where c.novel.novelId=?1")
    List<CommentEntity> findByNovelId(Long novelId);

    @Query("select distinct c from CommentEntity c left join fetch c.likeComments where c.id=?1")
    Optional<CommentEntity> findByIdWithLikeComment(Long id);
}
