package com.zakrzewski.reservationsystem.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(setterPrefix = "with")
public record RoomReservationUpdateRequest(
        Long roomId,
        Long authorId,
        String reservationTitle,
        @FutureOrPresent(message = "Start time must be in the future or present")
        LocalDateTime startTime,

        @FutureOrPresent(message = "End time must be in the future or present")
        LocalDateTime endTime,

        Boolean isPrivate,
        String description,
        List<Long> employeeList
) {
}
