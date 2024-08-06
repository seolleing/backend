package com.out4ider.selleing_backend.domain.gameroom.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameRoomRequestDto {
    String title;
    byte maxHeadCount;
    String password;
    String startSentence;
}
