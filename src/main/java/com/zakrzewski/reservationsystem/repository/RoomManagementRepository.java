package com.zakrzewski.reservationsystem.repository;

import com.zakrzewski.reservationsystem.model.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomManagementRepository extends JpaRepository<Long, RoomEntity> {
}
