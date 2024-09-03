package com.out4ider.selleing_backend.domain.gameroom.repository;

import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;

import java.util.Optional;

public interface CustomeGameRoomRepository {
    Optional<GameRoomEntity> findByIdWithUser(Long roomId);

}
