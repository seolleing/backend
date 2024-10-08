package com.out4ider.selleing_backend.domain.comment.repository;

import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {
    Optional<CommentEntity> findByIdWithUser(Long id);

    List<CommentResponseDto> findOrderByCommentId(Long novelId, Long lastId);

    List<CommentResponseDto> findOrderByCommentId(Long novelId);
}
