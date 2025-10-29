package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.dto.request.RoomCreateRequest;
import com.zakrzewski.reservationsystem.dto.response.RoomResponse;
import com.zakrzewski.reservationsystem.service.RoomManagementService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/room-management")
public class RoomManagementController {

    private final RoomManagementService roomManagementService;

    @SuppressWarnings("unused")
    public RoomManagementController() {
        this(null);
    }

    @Autowired
    public RoomManagementController(final RoomManagementService roomManagementService) {
        this.roomManagementService = roomManagementService;
    }

    @PostMapping(value = "/create-room", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomResponse> createRoom(@NotNull @RequestBody final RoomCreateRequest roomCreateRequest) {
        final RoomResponse createdRoom = roomManagementService.createRoom(roomCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRoom);
    }
}
