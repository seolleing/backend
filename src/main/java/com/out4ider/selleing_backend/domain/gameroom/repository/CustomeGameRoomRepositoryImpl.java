package com.out4ider.selleing_backend.domain.gameroom.repository;

import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomInquiryResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.QGameRoomInquiryResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.out4ider.selleing_backend.domain.gameroom.entity.QGameRoomEntity.gameRoomEntity;
import static com.out4ider.selleing_backend.domain.user.entity.QUserEntity.userEntity;


@RequiredArgsConstructor
public class CustomeGameRoomRepositoryImpl implements CustomeGameRoomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final int PAGE_SIZE = 20;

    @Override
    public Optional<GameRoomEntity> findByIdWithUser(Long roomId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(gameRoomEntity)
                        .join(gameRoomEntity.user, userEntity)
                        .fetchJoin()
                        .where(gameRoomEntity.id.eq(roomId))
                        .fetchOne()
        );
    }

    @Override
    public List<GameRoomInquiryResponseDto> findByIsStartedFalseOrderByGameRoomId(Long lastId) {
        return jpaQueryFactory
                .select(new QGameRoomInquiryResponseDto(
                        gameRoomEntity.title,
                        gameRoomEntity.startSentence,
                        gameRoomEntity.password,
                        gameRoomEntity.id,
                        gameRoomEntity.maxHeadCount))
                .from(gameRoomEntity)
                .where(gameRoomIdLt(lastId), gameRoomEntity.isStarted.isFalse())
                .orderBy(gameRoomEntity.id.desc())
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression gameRoomIdLt(Long gameRoomId) {
        return gameRoomId != null ? gameRoomEntity.id.lt(gameRoomId) : null;
    }
}
