package com.zakrzewski.reservationsystem.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(setterPrefix = "with")
public record RoomReservationResponse (
        Long roomId,
        boolean isPrivate,
        LocalDateTime endTime,
        Long roomReservationId,
        LocalDateTime startTime,
        String reservationTitle,
        List<String> employeeList
) {
}
