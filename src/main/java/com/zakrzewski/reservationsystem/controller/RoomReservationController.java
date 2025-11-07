package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.dto.request.RoomReservationRequest;
import com.zakrzewski.reservationsystem.dto.request.RoomReservationUpdateRequest;
import com.zakrzewski.reservationsystem.dto.response.RoomReservationResponse;
import com.zakrzewski.reservationsystem.service.RoomReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/room-reservation")
public class RoomReservationController {
    private final RoomReservationService roomReservationService;

    @SuppressWarnings("unused")
    public RoomReservationController() {
        this(null);
    }

    @Autowired
    public RoomReservationController(final RoomReservationService roomReservationService) {
        this.roomReservationService = roomReservationService;
    }


    @PostMapping(path = "/create-reservation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomReservationResponse> createReservation(@RequestBody @Valid final RoomReservationRequest roomReservationRequest) {
        final RoomReservationResponse roomReservationResponse = roomReservationService.createReservation(roomReservationRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomReservationResponse);
    }

    @GetMapping(path = "/all-reservation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomReservationResponse>> getAllReservation() {
        final List<RoomReservationResponse> roomReservationsList = roomReservationService.getAllReservation();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomReservationsList);
    }

    @GetMapping(path = "/get-reservation/{roomReservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomReservationResponse> getRoomReservationByReservationId(@PathVariable("roomReservationId") @NotNull final Long roomReservationId) {
        final RoomReservationResponse roomReservationResponse = roomReservationService.getRoomReservationByReservationId(roomReservationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomReservationResponse);
    }

    @GetMapping(path = "/get-reservation-list-by-room/{roomId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomReservationResponse>> getReservationsListByRoomId(@PathVariable("roomId") @NotNull final Long roomId) {
        final List<RoomReservationResponse> roomReservationResponseList = roomReservationService.getReservationsListByRoomId(roomId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomReservationResponseList);
    }

    @PatchMapping(path = "/update-reservation/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomReservationResponse> updateReservation(@PathVariable final Long reservationId,
                                                                     @RequestBody @Valid final RoomReservationUpdateRequest updateRequest) {
        final RoomReservationResponse roomReservationResponse = roomReservationService.updateReservation(reservationId, updateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomReservationResponse);
    }

    @DeleteMapping(path = "/delete-reservation/{roomReservationId}")
    public ResponseEntity<Long> deleteReservation(@PathVariable("roomReservationId") @NotNull final Long roomReservationId) {
        final Long roomId = roomReservationService.deleteReservation(roomReservationId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(roomId);
    }
}
