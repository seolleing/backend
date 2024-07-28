package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikeCommentRepository extends JpaRepository<LikeCommentEntity, Long> {
    @Modifying
    @Query("delete from LikeCommentEntity l where l.comment.id=?1 and l.user.email=?2")
    void deleteByCommentIdAndEmail(Long id, String email);
}
