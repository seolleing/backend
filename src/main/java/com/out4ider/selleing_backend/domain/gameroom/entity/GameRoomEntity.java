package com.out4ider.selleing_backend.domain.gameroom.entity;

import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomInquiryResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomRequestDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomSaveResponseDto;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_room")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GameRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "title")
    private String title;

    @Column(name = "max_head_count")
    private String maxHeadCount;

    @Column(name = "game_room_password")
    private String password;

    @Column(name = "game_room_code")
    private String code;

    @Column(name = "start_sentence")
    private String startSentence;

    @Column(name = "is_started")
    private boolean isStarted;

    public GameRoomSaveResponseDto toGameRoomSaveResponseDto() {
        return GameRoomSaveResponseDto.builder()
                .roomId(this.id)
                .roomCode(this.code)
                .build();
    }

    public void updateGameRoomEntity(GameRoomRequestDto gameRoomRequestDto) {
        this.title = gameRoomRequestDto.getTitle();
        this.maxHeadCount = gameRoomRequestDto.getMaxHeadCount();
        this.startSentence = gameRoomRequestDto.getStartSentence();
        this.password = gameRoomRequestDto.getPassword();
    }

    public GameRoomInquiryResponseDto toGameRoomInquiryResponseDto(String newHeadCount) {
        return new GameRoomInquiryResponseDto(
                this.title, this.startSentence,
                this.id, this.password,
                this.maxHeadCount, newHeadCount);
    }
}
