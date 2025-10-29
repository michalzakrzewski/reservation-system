package com.zakrzewski.reservationsystem.dto.response;

import lombok.Builder;

import java.util.Map;

@Builder(setterPrefix = "with")
public record RoomResponse (
        Long roomId,
        String roomName,
        String roomDescription,
        Integer roomCapacity,
        Map<String, String> userRoomDetails) {
}
