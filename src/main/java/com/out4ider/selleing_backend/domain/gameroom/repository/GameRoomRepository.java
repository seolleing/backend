package com.out4ider.selleing_backend.domain.gameroom.repository;

import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRoomRepository extends JpaRepository<GameRoomEntity, Long>, CustomeGameRoomRepository {
    List<GameRoomEntity> findByIsStartedFalse(Pageable pageable);

    Optional<GameRoomEntity> findByIsStartedFalseAndCode(String code);
}
