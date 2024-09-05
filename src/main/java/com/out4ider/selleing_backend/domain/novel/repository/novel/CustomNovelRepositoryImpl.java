package com.out4ider.selleing_backend.domain.novel.repository.novel;

import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.QNovelResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.out4ider.selleing_backend.domain.bookmark.entity.QBookmarkEntity.bookmarkEntity;
import static com.out4ider.selleing_backend.domain.novel.entity.QNovelEntity.novelEntity;
import static com.out4ider.selleing_backend.domain.user.entity.QUserEntity.userEntity;

@RequiredArgsConstructor
public class CustomNovelRepositoryImpl implements CustomNovelRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final int PAGE_SIZE = 20;

    @Override
    public Integer findLikeCountByNovelId(Long novelId) {
        return jpaQueryFactory
                .select(novelEntity.likeCount)
                .from(novelEntity)
                .where(novelEntity.novelId.eq(novelId))
                .fetchOne();
    }

    @Override
    public List<NovelResponseDto> findOrderByNovelId(Long lastId) {
        return jpaQueryFactory
                .select(new QNovelResponseDto(
                        novelEntity.novelId, novelEntity.title,
                        novelEntity.startSentence, novelEntity.likeCount))
                .from(novelEntity)
                .where(novelIdLt(lastId))
                .orderBy(novelEntity.novelId.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    @Override
    public List<NovelResponseDto> findOrderByNovelIdInBookmark(Long lastId, Long userId) {
        return jpaQueryFactory
                .select(new QNovelResponseDto(
                        novelEntity.novelId, novelEntity.title,
                        novelEntity.startSentence, novelEntity.likeCount))
                .from(novelEntity)
                .join(novelEntity.bookmarks, bookmarkEntity)
                .join(bookmarkEntity.user, userEntity)
                .where(novelIdLt(lastId), userIdEq(userId))
                .orderBy(novelEntity.novelId.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    @Override
    public List<NovelResponseDto> findOrderByLikeCount(Long lastId, Integer likeCount) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(likeCount!=null){
            booleanBuilder.and(novelEntity.likeCount.lt(likeCount));
        }
        if(lastId!=null){
            booleanBuilder.or(
                    novelEntity.likeCount.eq(likeCount)
                            .and(novelEntity.novelId.lt(lastId))
            );
        }
        return jpaQueryFactory
                .select(new QNovelResponseDto(
                        novelEntity.novelId, novelEntity.title,
                        novelEntity.startSentence, novelEntity.likeCount))
                .from(novelEntity)
                .where(booleanBuilder)
                .orderBy(novelEntity.likeCount.desc(), novelEntity.novelId.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression novelIdLt(Long novelId) {
        return novelId != null ? novelEntity.novelId.lt(novelId) : null;
    }

    private BooleanExpression userIdEq(Long userId) {
        return userId != null ? userEntity.userId.eq(userId) : null;
    }
}
