package com.zakrzewski.reservationsystem.service;

import com.zakrzewski.reservationsystem.dto.request.RoomCreateRequest;
import com.zakrzewski.reservationsystem.dto.response.RoomResponse;
import com.zakrzewski.reservationsystem.repository.RoomManagementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomManagementService {
    private static final Logger LOG = LoggerFactory.getLogger(RoomManagementService.class);

    private final RoomManagementRepository roomManagementRepository;

    @SuppressWarnings("unused")
    public RoomManagementService() {
        this(null);
    }

    @Autowired
    public RoomManagementService(final RoomManagementRepository roomManagementRepository) {
        this.roomManagementRepository = roomManagementRepository;
    }

    public RoomResponse createRoom(final RoomCreateRequest createRequest) {

    }
}
