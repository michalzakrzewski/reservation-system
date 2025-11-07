package com.zakrzewski.reservationsystem.repository;

import com.zakrzewski.reservationsystem.model.entity.RoomReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomReservationRepository extends JpaRepository<RoomReservationEntity, Long> {
    List<RoomReservationEntity> findByRoomId(Long roomId);
    List<RoomReservationEntity> findByRoomIdAndStartTimeBeforeAndEndTimeAfter(Long roomId, LocalDateTime endTime, LocalDateTime startTime);
}
