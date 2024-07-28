package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepository extends JpaRepository<LikeCommentEntity, Long> {
}
