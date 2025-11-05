package com.zakrzewski.reservationsystem.mapper;

import com.zakrzewski.reservationsystem.dto.request.RoomCreateRequest;
import com.zakrzewski.reservationsystem.dto.request.RoomReservationRequest;
import com.zakrzewski.reservationsystem.dto.response.RoomReservationResponse;
import com.zakrzewski.reservationsystem.dto.response.RoomResponse;
import com.zakrzewski.reservationsystem.model.entity.EmployeeEntity;
import com.zakrzewski.reservationsystem.model.entity.RoomEntity;
import com.zakrzewski.reservationsystem.model.entity.RoomReservationEntity;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public RoomReservationResponse mapRoomReservationEntityToRoomReservationResponse(final RoomReservationEntity roomReservationEntity) {
        final List<String> employeeList = roomReservationEntity.getEmplployeesList().stream()
                .map(employeeEntity -> employeeEntity.getFirstName() + " " + employeeEntity.getLastName())
                .toList();
        return RoomReservationResponse.builder()
                .withRoomId(roomReservationEntity.getRoomId())
                .withRoomReservationId(roomReservationEntity.getRoomReservationId())
                .withReservationTitle(roomReservationEntity.getReservationTitle())
                .withStartTime(roomReservationEntity.getStartTime())
                .withEndTime(roomReservationEntity.getEndTime())
                .withEmployeeList(employeeList)
                .build();
    }

    public RoomReservationEntity mapRoomReservationRequestToRoomReservationEntity(final List<EmployeeEntity> employeesList,
                                                                                  final RoomReservationRequest roomReservationRequest) {
        return RoomReservationEntity.builder()
                .withRoomId(roomReservationRequest.roomId())
                .withAuthorId(roomReservationRequest.authorId())
                .withReservationTitle(roomReservationRequest.reservationTitle())
                .withStartTime(roomReservationRequest.startTime())
                .withEndTime(roomReservationRequest.endTime())
                .withDescription(roomReservationRequest.description())
                .withEmplployeesList(employeesList)
                .build();
    }
}
