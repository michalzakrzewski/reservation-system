package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.dto.request.RoomCreateRequest;
import com.zakrzewski.reservationsystem.dto.response.RoomResponse;
import com.zakrzewski.reservationsystem.service.RoomManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping(path = "/create-room", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomResponse> createRoom(@RequestBody @Valid final RoomCreateRequest roomCreateRequest) {
        final RoomResponse createdRoom = roomManagementService.createRoom(roomCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRoom);
    }

    @GetMapping(path = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        final List<RoomResponse> allRooms = roomManagementService.getAllRooms();
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping(path = "/find-by-room-name/{roomName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomResponse> getRoomByName(@PathVariable("roomName") final String roomName) {
        final RoomResponse roomByName = roomManagementService.getRoomByName(roomName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomByName);
    }
}
