package com.zakrzewski.reservationsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Map;

@Builder(setterPrefix = "with")
public record RoomCreateRequest (
        @NotBlank(message = "Room name can not be blank")
        String roomName,

        @NotBlank(message = "Room description can not be blank")
        String roomDescription,

        @NotNull(message = "Room capacity can not be blank")
        Integer roomCapacity,

        @NotNull(message = "Room user details can not be blank")
        Map<String, String> userRoomDetails) {
}
