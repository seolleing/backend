package com.out4ider.selleing_backend.domain.gameroom.repository;

import com.out4ider.selleing_backend.domain.gameroom.entity.GameRoomEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GameRoomRepository extends JpaRepository<GameRoomEntity, Long> {
    @Query("select r from GameRoomEntity r where r.isStarted=false")
    List<GameRoomEntity> findAllByIsStarted(Pageable pageable);

    @Query("select r from GameRoomEntity r join fetch r.user where r.id=?1")
    Optional<GameRoomEntity> findByIdWithUser(Long roomId);

//    @Query("select r from GameRoomEntity r where r.isStarted=false order by r.id limit 1")
//    Optional<GameRoomEntity> findByRandom();

    @Query("select r from GameRoomEntity r where r.isStarted=false and r.code=?1")
    Optional<GameRoomEntity> findByCode(String code);
}
