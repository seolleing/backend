package com.out4ider.selleing_backend.domain.gameroom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameRoomInquiryResponseDto {
    private String title;
    private String startSentence;
    private Long roomId;
    private int maxHeadCount;
    private int currentHeadCount;
}
