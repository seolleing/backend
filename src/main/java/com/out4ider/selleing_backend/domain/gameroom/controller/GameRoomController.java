package com.out4ider.selleing_backend.domain.gameroom.controller;

import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomInquiryResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomRequestDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomSaveResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.service.GameRoomService;
import com.out4ider.selleing_backend.global.common.dto.ResponseDto;
import com.out4ider.selleing_backend.global.security.SimpleCustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gameRooms")
@RequiredArgsConstructor
public class GameRoomController {
    private final GameRoomService gameRoomService;

    @PostMapping
    public ResponseEntity<?> saveGameRoom(@RequestBody GameRoomRequestDto gameRoomRequestDto, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails) {
        GameRoomSaveResponseDto gameRoomSaveResponseDto = gameRoomService.save(gameRoomRequestDto, simpleCustomUserDetails.getUserId());
        return ResponseDto.onSuccess(gameRoomSaveResponseDto);
    }

    @GetMapping
    public ResponseEntity<?> getGameRooms(@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        List<GameRoomInquiryResponseDto> gameRoomInquiryResponseDtos = gameRoomService.getSome(page);
        return ResponseDto.onSuccess(gameRoomInquiryResponseDtos);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteGameRoom(@PathVariable(name = "roomId") Long roomId, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails) {
        gameRoomService.delete(roomId, simpleCustomUserDetails.getUserId());
        return ResponseDto.onSuccess();
    }

//    @GetMapping("/random")
//    public ResponseEntity<?> getRandomGameRoom() {
//        GameRoomInquiryResponseDto gameRoomInquiryResponseDto = gameRoomService.getRandomRoom();
//        return ResponseEntity.ok().body(gameRoomInquiryResponseDto);
//    }

    @GetMapping("/friend")
    public ResponseEntity<?> getFriendRoom(@RequestParam("code") String code) {
        GameRoomInquiryResponseDto gameRoomInquiryResponseDto = gameRoomService.getFriendRoom(code);
        return ResponseDto.onSuccess(gameRoomInquiryResponseDto);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable(name = "roomId") Long roomId) {
        GameRoomInquiryResponseDto gameRoomInquiryResponseDto = gameRoomService.getRoom(roomId);
        return ResponseDto.onSuccess(gameRoomInquiryResponseDto);
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<?> leaveRoom(@PathVariable(name = "roomId") Long roomId) {
        gameRoomService.leave(roomId);
        return ResponseDto.onSuccess();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable(name = "roomId") Long roomId,
                                        @RequestBody GameRoomRequestDto gameRoomRequestDto, @AuthenticationPrincipal SimpleCustomUserDetails simpleCustomUserDetails) {
        gameRoomService.update(roomId, gameRoomRequestDto, simpleCustomUserDetails.getUserId());
        return ResponseDto.onSuccess();
    }
}
