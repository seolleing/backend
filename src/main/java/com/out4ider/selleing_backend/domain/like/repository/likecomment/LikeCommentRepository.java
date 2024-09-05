package com.out4ider.selleing_backend.domain.like.repository.likecomment;

import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository
        extends JpaRepository<LikeCommentEntity, Long>, CustomLikeCommentRepository {
    boolean existsByComment_IdAndUser_UserId(Long commentId, Long userId);
}
