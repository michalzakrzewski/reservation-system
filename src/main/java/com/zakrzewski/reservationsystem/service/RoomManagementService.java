package com.zakrzewski.reservationsystem.service;

import com.zakrzewski.reservationsystem.dto.request.RoomCreateRequest;
import com.zakrzewski.reservationsystem.dto.response.RoomResponse;
import com.zakrzewski.reservationsystem.exceptions.ConflictException;
import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;
import com.zakrzewski.reservationsystem.exceptions.NotFoundException;
import com.zakrzewski.reservationsystem.mapper.RoomManagementMapper;
import com.zakrzewski.reservationsystem.entity.RoomEntity;
import com.zakrzewski.reservationsystem.repository.RoomManagementRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomManagementService {
    private static final Logger LOG = LoggerFactory.getLogger(RoomManagementService.class);

    private final RoomManagementMapper roomManagementMapper;
    private final RoomManagementRepository roomManagementRepository;

    @SuppressWarnings("unused")
    public RoomManagementService() {
        this(null, null);
    }

    @Autowired
    public RoomManagementService(final RoomManagementMapper roomManagementMapper,
                                 final RoomManagementRepository roomManagementRepository) {
        this.roomManagementMapper = roomManagementMapper;
        this.roomManagementRepository = roomManagementRepository;
    }

    @Caching(
            put = {
                    @CachePut(value = "room", key = "#result.roomId")
            },
            evict = {
                    @CacheEvict(value = "room", key = "'all'"),
                    @CacheEvict(value = "room", key = "#result.roomName"),
            }
    )
    public RoomResponse createRoom(final RoomCreateRequest roomCreateRequest) {
        final String roomName = roomCreateRequest.roomName();
        LOG.info("Creating room with name: {}", roomName);
        try {
            final RoomEntity createdRoom = roomManagementMapper.mapRoomCreateRequestToRoomEntity(roomCreateRequest);
            final RoomEntity savedRoom = roomManagementRepository.save(createdRoom);
            final RoomResponse roomResponse = roomManagementMapper.mapRoomEntityToRoomResponse(savedRoom);
            LOG.info("New room has been saved successfully with id: {}", roomResponse.roomId());
            return roomResponse;
        } catch (DataIntegrityViolationException dive) {
            LOG.error("DataIntegrityViolationException caught while saving room with name {}. Possible constraint violation, message: {}", roomName, dive.getMessage(), dive);
            throw new ConflictException("Constraint violation while creating employee");
        }
    }

    @Cacheable(value = "room", key = "'all'")
    public List<RoomResponse> getAllRooms() {
        final List<RoomEntity> roomList = roomManagementRepository.findAll();
        if (roomList.isEmpty()) {
            LOG.warn("No rooms found in database");
            return List.of();
        }
        return roomList.stream()
                .map(roomManagementMapper::mapRoomEntityToRoomResponse)
                .toList();
    }

    @Cacheable(value = "room", key = "#roomName")
    public RoomResponse getRoomByName(final String roomName) {
        if (StringUtils.isBlank(roomName)) {
            LOG.warn("Room name is blank during get room by name");
            throw new InvalidInputException("Room name is blank");
        }
        final Optional<RoomEntity> room = roomManagementRepository.findByRoomName(roomName);
        return room.map(roomManagementMapper::mapRoomEntityToRoomResponse)
                .orElseThrow(() -> {
                    LOG.warn("Room: {} not found in database", roomName);
                    return new NotFoundException("Room not found");
                });
    }
}
