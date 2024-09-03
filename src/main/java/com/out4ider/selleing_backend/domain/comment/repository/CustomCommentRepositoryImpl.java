package com.out4ider.selleing_backend.domain.comment.repository;

import com.out4ider.selleing_backend.domain.comment.dto.CommentResponseDto;
import com.out4ider.selleing_backend.domain.comment.dto.QCommentResponseDto;
import com.out4ider.selleing_backend.domain.comment.entity.CommentEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.out4ider.selleing_backend.domain.comment.entity.QCommentEntity.commentEntity;
import static com.out4ider.selleing_backend.domain.user.entity.QUserEntity.userEntity;


@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<CommentEntity> findByIdWithUser(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(commentEntity)
                        .where(commentEntity.id.eq(id))
                        .join(commentEntity.user)
                        .fetchJoin()
                        .fetchOne()
        );
    }

    @Override
    public List<CommentResponseDto> findByNovelId(Long novelId, Long lastCommentId) {
        return jpaQueryFactory
                .select(new QCommentResponseDto(
                        commentEntity.id,
                        commentEntity.content,
                        userEntity.nickname,
                        commentEntity.likeCount
                ))
                .from(commentEntity)
                .join(commentEntity.user, userEntity)
                .where(commentIdGt(lastCommentId),
                        commentEntity.novel.novelId.eq(novelId))
                .limit(20)
                .fetch();
    }

    @Override
    public List<CommentResponseDto> findByNovelId(Long novelId){
        return findByNovelId(novelId,null);
    }

    private BooleanExpression commentIdGt(Long commentId){
        return commentId!=null ? commentEntity.id.gt(commentId) : null;
    }
}
