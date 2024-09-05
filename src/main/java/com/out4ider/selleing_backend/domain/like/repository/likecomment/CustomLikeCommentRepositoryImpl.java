package com.out4ider.selleing_backend.domain.like.repository.likecomment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.out4ider.selleing_backend.domain.like.entity.QLikeCommentEntity.likeCommentEntity;

@RequiredArgsConstructor
public class CustomLikeCommentRepositoryImpl implements CustomLikeCommentRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteByCommentId(Long commentId) {
        jpaQueryFactory
                .delete(likeCommentEntity)
                .where(likeCommentEntity.comment.id.eq(commentId))
                .execute();
    }
}
