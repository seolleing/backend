package com.out4ider.selleing_backend.domain.gameroom.repository;

import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomInquiryResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;

import java.util.List;
import java.util.Optional;

public interface CustomeGameRoomRepository {
    Optional<GameRoomEntity> findByIdWithUser(Long roomId);

    List<GameRoomInquiryResponseDto> findByIsStartedFalseOrderByGameRoomId(Long lastId);
}
