package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.dto.request.RoomReservationRequest;
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
    public ResponseEntity<Boolean> createReservation(@RequestBody @Valid final RoomReservationRequest roomReservationRequest) {
        final Boolean isCreated = roomReservationService.createReservation(roomReservationRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(isCreated);
    }

    @GetMapping(path = "/all-reservation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomReservationResponse>> getAllReservation() {
        final List<RoomReservationResponse> roomReservationsList = roomReservationService.getAllReservation();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomReservationsList);
    }

    @GetMapping(path = "/get-reservation/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomReservationResponse> getRoomReservationByReservationId(@PathVariable("reservationId") @NotNull final Long reservationId) {
        final RoomReservationResponse roomReservationResponse = roomReservationService.getRoomReservationByReservationId(reservationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(roomReservationResponse);
    }
}
