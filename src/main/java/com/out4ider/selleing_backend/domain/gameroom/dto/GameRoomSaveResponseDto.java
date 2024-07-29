package com.out4ider.selleing_backend.domain.gameroom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameRoomSaveResponseDto {
    private Long roomId;
    private String roomCode;
}
