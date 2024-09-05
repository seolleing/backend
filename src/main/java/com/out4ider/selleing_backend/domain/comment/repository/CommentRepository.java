package com.out4ider.selleing_backend.domain.comment.repository;

import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository
        extends JpaRepository<CommentEntity, Long>, CustomCommentRepository {
}
