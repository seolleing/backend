package com.out4ider.selleing_backend.domain.gameroom.repository;

import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.out4ider.selleing_backend.domain.gameroom.entity.QGameRoomEntity.gameRoomEntity;
import static com.out4ider.selleing_backend.domain.user.entity.QUserEntity.userEntity;

@RequiredArgsConstructor
public class CustomeGameRoomRepositoryImpl implements CustomeGameRoomRepository {
    private final JPAQueryFactory jpaQueryFactory;

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
}
