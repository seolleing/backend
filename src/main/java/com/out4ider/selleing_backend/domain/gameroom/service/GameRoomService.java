package com.out4ider.selleing_backend.domain.gameroom.service;

import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomInquiryResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomRequestDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomSaveResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;
import com.out4ider.selleing_backend.domain.gameroom.repository.GameRoomRepository;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import com.out4ider.selleing_backend.domain.user.repository.UserRepository;
import com.out4ider.selleing_backend.global.common.service.RedisService;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotAuthorizedException;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Transactional
    public GameRoomSaveResponseDto save(GameRoomRequestDto gameRoomRequestDto, Long userId) {
        UserEntity userEntitiy = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundElementException(
                        ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        GameRoomEntity gameRoomEntity = GameRoomEntity.builder()
                .title(gameRoomRequestDto.getTitle())
                .maxHeadCount(gameRoomRequestDto.getMaxHeadCount())
                .isStarted(false).user(userEntitiy)
                .startSentence(gameRoomRequestDto.getStartSentence())
                .password(gameRoomRequestDto.getPassword())
                .code(UUID.randomUUID().toString())
                .build();
        gameRoomRepository.save(gameRoomEntity);
        redisService.setGameRoomInitialHeadCount(gameRoomEntity.getId());
        return gameRoomEntity.toGameRoomSaveResponseDto();
    }

    public List<GameRoomInquiryResponseDto> findGameRoomsByLatest(Long lastId) {
        List<GameRoomInquiryResponseDto> gameRoomResponseDtos = gameRoomRepository.findByIsStartedFalseOrderByGameRoomId(lastId);
        gameRoomResponseDtos.forEach(gameRoomInquiryResponseDto ->
                gameRoomInquiryResponseDto.setCurrentHeadCount(
                        redisService.getGameRoomHeadCount(gameRoomInquiryResponseDto.getRoomId())));
        return gameRoomResponseDtos;
    }

    @Transactional
    public void delete(Long roomId, Long userId) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findByIdWithUser(roomId)
                .orElseThrow(() -> new NotFoundElementException(
                        ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        if (gameRoomEntity.getUser().getUserId().equals(userId)) {
            gameRoomRepository.delete(gameRoomEntity);
            redisService.deleteGameRoomHeadCount(gameRoomEntity.getId());
        } else {
            throw new NotAuthorizedException(
                    ExceptionEnum.NOTAUTHORIZED.ordinal(), "you dont have authorization", HttpStatus.FORBIDDEN);
        }
    }

//    public GameRoomInquiryResponseDto getRandomRoom() {
//        GameRoomEntity gameRoomEntity = gameRoomRepository.findByRandom().orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
//        return joinRoom(gameRoomEntity);
//    }

    public GameRoomInquiryResponseDto findFriendRoom(String code) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundElementException(
                        ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "없는 방입니다.", HttpStatus.LOCKED));
        return joinRoom(gameRoomEntity);
    }

    public GameRoomInquiryResponseDto findGameRoom(Long roomId) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundElementException(
                        ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "없는 방입니다.", HttpStatus.LOCKED));
        return joinRoom(gameRoomEntity);
    }

    public void leave(Long roomId) {
        redisService.subGameRoomHeadCount(roomId);
    }

    @Transactional
    public void update(Long roomId, GameRoomRequestDto gameRoomRequestDto, Long userId) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundElementException(
                        ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "없는 방입니다.", HttpStatus.LOCKED));
        if (gameRoomEntity.getUser().getUserId().equals(userId)) {
            gameRoomEntity.updateGameRoomEntity(gameRoomRequestDto);
            gameRoomRepository.save(gameRoomEntity);
        } else {
            throw new NotAuthorizedException(
                    ExceptionEnum.NOTAUTHORIZED.ordinal(), "you dont have authorization", HttpStatus.FORBIDDEN);
        }
    }

    private GameRoomInquiryResponseDto joinRoom(GameRoomEntity gameRoomEntity) {
        if (gameRoomEntity.isStarted()) {
            throw new RuntimeException("이미 시작했당");
        }
        String currentHeadCount = redisService.getGameRoomHeadCount(gameRoomEntity.getId());
        if (currentHeadCount.compareTo(gameRoomEntity.getMaxHeadCount()) >= 0) {
            throw new RuntimeException("꽉찼당");
        }
        Long newHeadCount = redisService.addGameRoomHeadCount(gameRoomEntity.getId());
        return gameRoomEntity.toGameRoomInquiryResponseDto(newHeadCount.toString());
    }

}
