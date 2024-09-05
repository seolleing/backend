package com.out4ider.selleing_backend.domain.gameroom.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRoomInquiryResponseDto {
    private String title;
    private String startSentence;
    private Long roomId;
    private String password;
    private String maxHeadCount;
    private String currentHeadCount;

    @QueryProjection
    public GameRoomInquiryResponseDto(String title, String startSentence,
                                      String password, Long roomId, String maxHeadCount) {
        this.title = title;
        this.startSentence = startSentence;
        this.password = password;
        this.roomId = roomId;
        this.maxHeadCount = maxHeadCount;
    }
}
