package com.out4ider.selleing_backend.domain.novel.repository;

import com.out4ider.selleing_backend.domain.novel.dto.NovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.dto.QNovelResponseDto;
import com.out4ider.selleing_backend.domain.novel.entity.NovelEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Supplier;

import static com.out4ider.selleing_backend.domain.novel.entity.QNovelEntity.novelEntity;

@RequiredArgsConstructor
public class CustomNovelRepositoryImpl implements CustomNovelRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<NovelEntity> findAllWithBookmark(Pageable pageable, Long userId) {
        return null;
    }

    @Override
    public List<NovelResponseDto> findAllOrderByNovelId(Long lastId) {
        return jpaQueryFactory
                .select(new QNovelResponseDto(
                        novelEntity.novelId,
                        novelEntity.title,
                        novelEntity.startSentence,
                        novelEntity.likeCount
                ))
                .from(novelEntity)
                .where(novelIdLt(lastId))
                .orderBy(novelEntity.novelId.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public List<NovelResponseDto> findAllOrderByLikeCount(Integer likeCount, Long lastId) {
        return jpaQueryFactory
                .select(new QNovelResponseDto(
                        novelEntity.novelId,
                        novelEntity.title,
                        novelEntity.startSentence,
                        novelEntity.likeCount
                ))
                .from(novelEntity)
                .where(likeCountLt(likeCount)
                        .or(likeCountEq(likeCount).and(novelIdLt(lastId))))
                .orderBy(novelEntity.likeCount.desc(), novelEntity.novelId.desc())
                .limit(20)
                .fetch();
    }

    private BooleanExpression novelIdLt(Long novelId) {
        return novelId != null ? novelEntity.novelId.lt(novelId) : null;
    }

    private BooleanBuilder likeCountLt(Integer likeCount) {
        return nullSafeBuilder(()->novelEntity.likeCount.lt(likeCount));
    }

    private BooleanBuilder likeCountEq(Integer likeCount) {
        return nullSafeBuilder(()->novelEntity.likeCount.eq(likeCount));
    }

    private BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
        try {
            return new BooleanBuilder(f.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }
}
