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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRoomService {
    private final GameRoomRepository gameRoomRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Integer> stringIntegerRedisTemplate;

    @Transactional
    public GameRoomSaveResponseDto save(GameRoomRequestDto gameRoomRequestDto, String email) {
        UserEntity userEntitiy = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
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
        stringIntegerRedisTemplate.opsForValue().set("room" + gameRoomEntity.getId(), 1);
        return gameRoomEntity.toGameRoomSaveResponseDto();
    }

    public List<GameRoomInquiryResponseDto> getSome(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return gameRoomRepository.findAllByIsStarted(pageable).stream().map((gameRoomEntity) -> {
            int currentHeadCount = getCurrentHeadCount(gameRoomEntity.getId());;
            return gameRoomEntity.toGameRoomInquiryResponseDto(currentHeadCount);
        }).toList();
    }

    @Transactional
    public void delete(Long roomId, String email) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findByIdWithUser(roomId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "This is not in DB", HttpStatus.LOCKED));
        if (gameRoomEntity.getUser().getEmail().equals(email)) {
            gameRoomRepository.delete(gameRoomEntity);
            stringIntegerRedisTemplate.delete("room" + gameRoomEntity.getId());
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
        GameRoomEntity gameRoomEntity = gameRoomRepository.findByCode(code).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "방이 꽉찼습니다.", HttpStatus.LOCKED));
        return joinRoom(gameRoomEntity);
    }

    @Transactional
    public GameRoomInquiryResponseDto getRoom(Long roomId) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findById(roomId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "방이 꽉찼습니다.", HttpStatus.LOCKED));
        return joinRoom(gameRoomEntity);
    }

    @Transactional
    public void leave(Long roomId) {
        int currentHeadCount = getCurrentHeadCount(roomId);
        stringIntegerRedisTemplate.opsForValue().set("room" + roomId, currentHeadCount - 1);
    }

    public void update(Long roomId, GameRoomRequestDto gameRoomRequestDto, String email) {
        GameRoomEntity gameRoomEntity = gameRoomRepository.findById(roomId).orElseThrow(() -> new NotFoundElementException(ExceptionEnum.NOTFOUNDELEMENT.ordinal(), "방이 꽉찼습니다.", HttpStatus.LOCKED));
        if (gameRoomEntity.getUser().getEmail().equals(email)) {
            gameRoomEntity.updateGameRoomEntity(gameRoomRequestDto);
        } else {
            throw new NotAuthorizedException(ExceptionEnum.NOTAUTHORIZED.ordinal(), "you dont have authorization", HttpStatus.FORBIDDEN);
        }
    }

    private GameRoomInquiryResponseDto joinRoom(GameRoomEntity gameRoomEntity) {
        int currentHeadCount = getCurrentHeadCount(gameRoomEntity.getId());
        if (currentHeadCount >= gameRoomEntity.getMaxHeadCount()) {
            throw new RuntimeException("꽉찼당");
        }
        if (gameRoomEntity.isStarted()) {
            throw new RuntimeException("이미 시작했당");
        }
        stringIntegerRedisTemplate.opsForValue().set("room" + gameRoomEntity.getId(), currentHeadCount + 1);
        return gameRoomEntity.toGameRoomInquiryResponseDto(currentHeadCount + 1);
    }

    private int getCurrentHeadCount(Long roomId){
        return Objects.requireNonNull(stringIntegerRedisTemplate.opsForValue().get("room" + roomId), "다시 시도 가즈아");
    }
}
