package com.out4ider.selleing_backend.domain.gameroom.repository;

import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRoomRepository
        extends JpaRepository<GameRoomEntity, Long>, CustomeGameRoomRepository {
    Optional<GameRoomEntity> findByCode(String code);
}
