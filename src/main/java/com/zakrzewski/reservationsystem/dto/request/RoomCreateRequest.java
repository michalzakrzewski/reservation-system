package com.zakrzewski.reservationsystem.dto.request;

import lombok.Builder;

import java.util.Map;

@Builder(setterPrefix = "with")
public record RoomCreateRequest (
    String roomName,
    String roomDescription,
    Integer roomCapacity,
    Map<String, String> userRoomDetails) {
}
