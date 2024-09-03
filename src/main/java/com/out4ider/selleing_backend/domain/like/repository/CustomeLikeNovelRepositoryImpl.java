package com.out4ider.selleing_backend.domain.like.repository;

import com.out4ider.selleing_backend.domain.like.entity.LikeNovelEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.out4ider.selleing_backend.domain.like.entity.QLikeNovelEntity.likeNovelEntity;

@RequiredArgsConstructor
public class CustomeLikeNovelRepositoryImpl implements CustomeLikeNovelRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<LikeNovelEntity> findByNovelIdWithUser(Long novelId) {
        return jpaQueryFactory
                .selectFrom(likeNovelEntity)
                .join(likeNovelEntity.user)
                .fetchJoin()
                .where(likeNovelEntity.novel.novelId.eq(novelId))
                .fetch();
    }
}
