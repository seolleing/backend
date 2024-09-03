package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeCommentEntity, Long> {
    Optional<LikeCommentEntity> findByComment_IdAndUser_UserId(Long commentId, Long userId);

    void deleteByComment_id(Long id);
}
