package com.zakrzewski.reservationsystem.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(setterPrefix = "with")
public record RoomReservationRequest(
        @NotNull(message = "Room id can not be blank")
        Long roomId,

        @NotNull(message = "Author id can not be blank")
        Long authorId,

        @NotBlank(message = "Reservation title can not be blank")
        String reservationTitle,

        @NotNull(message = "Start time can not be null")
        @Future(message = "Start time must be in the future")
        LocalDateTime startTime,

        @NotNull(message = "End time can not be null")
        @Future(message = "End time must be in the future")
        LocalDateTime endTime,

        boolean isPrivate,
        String description,

        @NotEmpty(message = "Employee list cannot be empty")
        List<Long> employeeList
) {
}
