package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeCommentEntity, Long>, CustomLikeCommentRepository {
    @Query("select l from LikeCommentEntity l where l.comment.id=?1 and l.user.userId=?2")
    Optional<LikeCommentEntity> findLikeComment(Long commentId, Long userId);

    @Modifying
    @Query("delete from LikeCommentEntity l where l.comment.id=?1")
    void deleteLikeComment(Long id);
}
