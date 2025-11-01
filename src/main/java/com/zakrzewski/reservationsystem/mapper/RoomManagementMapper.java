package com.zakrzewski.reservationsystem.mapper;

import com.zakrzewski.reservationsystem.dto.request.RoomCreateRequest;
import com.zakrzewski.reservationsystem.dto.response.RoomResponse;
import com.zakrzewski.reservationsystem.model.RoomEntity;
import org.springframework.stereotype.Component;

@Component
public class RoomManagementMapper {

    public RoomManagementMapper() {
    }

    public RoomResponse mapRoomEntityToRoomResponse(final RoomEntity roomEntity) {
        return RoomResponse.builder()
                .withRoomId(roomEntity.getRoomId())
                .withRoomName(roomEntity.getRoomName())
                .withRoomCapacity(roomEntity.getRoomCapacity())
                .withRoomDescription(roomEntity.getRoomDescription())
                .withUserRoomDetails(roomEntity.getUserRoomDetails())
                .build();
    }

    public RoomEntity mapRoomCreateRequestToRoomEntity(final RoomCreateRequest roomCreateRequest) {
        return RoomEntity.builder()
                .withRoomName(roomCreateRequest.roomName())
                .withRoomCapacity(roomCreateRequest.roomCapacity())
                .withRoomDescription(roomCreateRequest.roomDescription())
                .withUserRoomDetails(roomCreateRequest.userRoomDetails())
                .build();
    }
}
