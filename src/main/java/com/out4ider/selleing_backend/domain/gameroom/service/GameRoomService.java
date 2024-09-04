package com.out4ider.selleing_backend.domain.gameroom.service;

import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomInquiryResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomRequestDto;
import com.out4ider.selleing_backend.domain.gameroom.dto.GameRoomSaveResponseDto;
import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;
import com.out4ider.selleing_backend.domain.gameroom.repository.GameRoomRepository;
import com.out4ider.selleing_backend.domain.user.entity.UserEntity;
import com.out4ider.selleing_backend.domain.user.repository.UserRepository;
import com.out4ider.selleing_backend.global.exception.ExceptionEnum;
import com.out4ider.selleing_backend.global.exception.kind.NotAuthorizedException;
import com.out4ider.selleing_backend.global.exception.kind.NotFoundElementException;
import com.out4ider.selleing_backend.global.common.service.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomService {
    private static final Logger log = LoggerFactory.getLogger(GameRoomService.class);
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;

    @Transactional
    public GameRoomSaveResponseDto save(GameRoomRequestDto gameRoomRequestDto, Long userId) {
        UserEntity userEntitiy = userRepository.findById(userId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        GameRoomEntity gameRoomEntity = GameRoomEntity.builder()
                .title(gameRoomRequestDto.getTitle())
                .maxHeadCount(gameRoomRequestDto.getMaxHeadCount())
                .isStarted(false)
                .startSentence(gameRoomRequestDto.getStartSentence())
                .password(gameRoomRequestDto.getPassword())
                .user(userEntitiy)
                .code(UUID.randomUUID().toString())
                .build();
        gameRoomRepository.save(gameRoomEntity);
        redisService.setGameRoomInitialHeadCount(gameRoomEntity.getId());
        return gameRoomEntity.toGameRoomSaveResponseDto();
    }

    public List<GameRoomInquiryResponseDto> getSome(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return gameRoomRepository.findByIsStartedFalse(pageable).stream().map((gameRoomEntity) -> {
            byte currentHeadCount = redisService.getGameRoomHeadCount(gameRoomEntity.getId());
            return gameRoomEntity.toGameRoomInquiryResponseDto(currentHeadCount);
        }).toList();
    }

    @Transactional
    public void delete(Long roomId, Long userId) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findByIdWithUser(roomId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        if (gameRoomEntity.getUser().getUserId().equals(userId)) {
            gameRoomRepository.delete(gameRoomEntity);
            redisService.deleteGameRoomHeadCount(gameRoomEntity.getId());
        } else {
            throw new NotAuthorizedException(ExceptionEnum.NOTAUTHORIZED.ordinal(), "you dont have authorization", HttpStatus.FORBIDDEN);
        }
    }

//    @Transactional
//    public GameRoomInquiryResponseDto getRandomRoom() {
//        GameRoomEntity gameRoomEntity = gameRoomRepository.findByRandom().orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
//        return joinRoom(gameRoomEntity);
//    }

    @Transactional
    public GameRoomInquiryResponseDto getFriendRoom(String code) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findByIsStartedFalseAndCode(code).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "방이 꽉찼습니다.", HttpStatus.LOCKED));
        return joinRoom(gameRoomEntity);
    }

    @Transactional
    public GameRoomInquiryResponseDto getRoom(Long roomId) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findById(roomId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "방이 꽉찼습니다.", HttpStatus.LOCKED));
        return joinRoom(gameRoomEntity);
    }

    @Transactional
    public void leave(Long roomId) {
        redisService.subGameRoomHeadCount(roomId);
    }

    @Transactional
    public void update(Long roomId, GameRoomRequestDto gameRoomRequestDto, Long userId) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findById(roomId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "방이 꽉찼습니다.", HttpStatus.LOCKED));
        if (gameRoomEntity.getUser().getUserId().equals(userId)) {
            gameRoomEntity.updateGameRoomEntity(gameRoomRequestDto);
        } else {
            throw new NotAuthorizedException(ExceptionEnum.NOTAUTHORIZED.ordinal(), "you dont have authorization", HttpStatus.FORBIDDEN);
        }
    }

    private GameRoomInquiryResponseDto joinRoom(GameRoomEntity gameRoomEntity) {
        byte currentHeadCount = redisService.getGameRoomHeadCount(gameRoomEntity.getId());
        if (currentHeadCount >= gameRoomEntity.getMaxHeadCount()) {
            throw new RuntimeException("꽉찼당");
        }
        if (gameRoomEntity.isStarted()) {
            throw new RuntimeException("이미 시작했당");
        }
        byte newHeadCount = redisService.addGameRoomHeadCount(gameRoomEntity.getId());
        return gameRoomEntity.toGameRoomInquiryResponseDto(newHeadCount);
    }

}
