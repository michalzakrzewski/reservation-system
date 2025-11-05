package com.zakrzewski.reservationsystem.repository;

import com.zakrzewski.reservationsystem.model.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomManagementRepository extends JpaRepository<RoomEntity, Long> {
    Optional<RoomEntity> findByRoomName(final String roomName);
}
