package com.zakrzewski.reservationsystem.service;

import com.zakrzewski.reservationsystem.dto.request.RoomReservationRequest;
import com.zakrzewski.reservationsystem.dto.request.RoomReservationUpdateRequest;
import com.zakrzewski.reservationsystem.dto.response.RoomReservationResponse;
import com.zakrzewski.reservationsystem.exceptions.ConflictException;
import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;
import com.zakrzewski.reservationsystem.exceptions.NotFoundException;
import com.zakrzewski.reservationsystem.mapper.RoomManagementMapper;
import com.zakrzewski.reservationsystem.entity.EmployeeEntity;
import com.zakrzewski.reservationsystem.entity.RoomEntity;
import com.zakrzewski.reservationsystem.entity.RoomReservationEntity;
import com.zakrzewski.reservationsystem.repository.EmployeeRepository;
import com.zakrzewski.reservationsystem.repository.RoomManagementRepository;
import com.zakrzewski.reservationsystem.repository.RoomReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomReservationService {
    private static final Logger LOG = LoggerFactory.getLogger(RoomReservationService.class);

    private final EmployeeRepository employeeRepository;
    private final RoomManagementMapper roomManagementMapper;
    private final RoomManagementRepository roomManagementRepository;
    private final RoomReservationRepository roomReservationRepository;

    @SuppressWarnings("unused")
    public RoomReservationService() {
        this(null, null, null, null);
    }

    @Autowired
    public RoomReservationService(final EmployeeRepository employeeRepository,
                                  final RoomManagementMapper roomManagementMapper,
                                  final RoomManagementRepository roomManagementRepository,
                                  final RoomReservationRepository roomReservationRepository) {
        this.employeeRepository = employeeRepository;
        this.roomManagementMapper = roomManagementMapper;
        this.roomManagementRepository = roomManagementRepository;
        this.roomReservationRepository = roomReservationRepository;
    }

    @Caching(
            put = {
                    @CachePut(value = "reservation-by-id", key = "#result.roomReservationId")
            },
            evict = {
                    @CacheEvict(value = "reservation-list-all", allEntries = true),
                    @CacheEvict(value = "reservation-list-by-room", key = "#result.roomId"),
            }
    )
    public RoomReservationResponse createReservation(final RoomReservationRequest roomReservationRequest) {
        checkIfRoomIsAvailable(roomReservationRequest);

        final List<EmployeeEntity> employeeList = employeeRepository.findAllById(roomReservationRequest.employeeList());

        if (employeeList.size() != roomReservationRequest.employeeList().size()) {
            LOG.warn("Some employees not found during reservation creation. Entity employeeList: {}, request employeeList: {}", employeeList, roomReservationRequest.employeeList());
            throw new NotFoundException("Some employees not found");
        }

        final RoomReservationEntity roomReservationEntity = roomManagementMapper.mapRoomReservationRequestToRoomReservationEntity(employeeList, roomReservationRequest);
        final RoomReservationEntity roomReservation = roomReservationRepository.save(roomReservationEntity);
        LOG.info("Created reservation: {}", roomReservation);
        return roomManagementMapper.mapRoomReservationEntityToRoomReservationResponse(roomReservation);
    }

    public void checkIfRoomIsAvailable(final RoomReservationRequest roomReservationRequest) {
        final Long roomId = roomReservationRequest.roomId();
        final LocalDateTime startTime = roomReservationRequest.startTime();
        final LocalDateTime endTime = roomReservationRequest.endTime();

        final Optional<RoomEntity> retrievedRoom = roomManagementRepository.findById(roomId);
        if (retrievedRoom.isEmpty()) {
            LOG.warn("Room not found during reservation creation. RoomId: {}", roomId);
            throw new NotFoundException("Room not found during create reservation");
        }

        if (startTime.isAfter(endTime) || startTime.isEqual(endTime)) {
            LOG.warn("The reservation start time cannot be after or equal to the end time. RoomId: {}, startTime: {}, endTime: {}", roomId, startTime, endTime);
            throw new InvalidInputException("The reservation start time cannot be after or equal to the end time");
        }

        final List<RoomReservationEntity> overlappingReservations = roomReservationRepository.findByRoomIdAndStartTimeBeforeAndEndTimeAfter(roomId, endTime, startTime);
        if (!overlappingReservations.isEmpty()) {
            LOG.warn("There is overlapping reservation. RoomId: {}, startTime: {}, endTime: {}", roomId, startTime, endTime);
            throw new ConflictException("There is overlapping reservation");
        }
    }

    @Caching(
            put = {
                    @CachePut(value = "reservation-by-id", key = "#result.roomReservationId")
            },
            evict = {
                    @CacheEvict(value = "reservation-list-all", allEntries = true),
                    @CacheEvict(value = "reservation-list-by-room", key = "#oldRoomId"),
                    @CacheEvict(value = "reservation-list-by-room", key = "#result.roomId"),
            }
    )
    public RoomReservationResponse updateReservation(final Long reservationId,
                                                     final RoomReservationUpdateRequest updateRequest) {
        final RoomReservationEntity existingReservation = roomReservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    LOG.warn("Reservation not found for update. ReservationId: {}", reservationId);
                    return new NotFoundException("Reservation not found for update");
                });

        final Long oldRoomId = existingReservation.getRoomId();
        updateExistingReservationFields(updateRequest, oldRoomId, existingReservation);

        final RoomReservationEntity updatedReservation = roomReservationRepository.save(existingReservation);
        LOG.info("Updated reservation: {}", updatedReservation);

        return roomManagementMapper.mapRoomReservationEntityToRoomReservationResponse(updatedReservation);
    }

    private void updateExistingReservationFields(final RoomReservationUpdateRequest updateRequest, final Long oldRoomId, final RoomReservationEntity existingReservation) {
        if (updateRequest.roomId() != null && !updateRequest.roomId().equals(oldRoomId)) {
            final RoomEntity newRoom = roomManagementRepository.findById(updateRequest.roomId())
                    .orElseThrow(() -> {
                        LOG.warn("Room not found during reservation update. RoomId: {}", updateRequest.roomId());
                        return new NotFoundException("Room not found during update reservation");
                    });
            existingReservation.setRoomId(newRoom.getRoomId());
        }

        if (updateRequest.reservationTitle() != null) {
            existingReservation.setReservationTitle(updateRequest.reservationTitle());
        }

        if (updateRequest.startTime() != null) {
            existingReservation.setStartTime(updateRequest.startTime());
        }

        if (updateRequest.endTime() != null) {
            existingReservation.setEndTime(updateRequest.endTime());
        }

        if (updateRequest.isPrivate() != null) {
            existingReservation.setPrivate(updateRequest.isPrivate());
        }

        if (updateRequest.description() != null) {
            existingReservation.setDescription(updateRequest.description());
        }

        if (updateRequest.employeeList() != null) {
            final List<EmployeeEntity> newEmployeeList = employeeRepository.findAllById(updateRequest.employeeList());

            if (newEmployeeList.size() != updateRequest.employeeList().size()) {
                LOG.warn("Some employees not found during reservation update.");
                throw new NotFoundException("Some employees not found");
            }
            existingReservation.setEmplployeesList(newEmployeeList);
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "reservation-list-all", allEntries = true),
                    @CacheEvict(value = "reservation-list-by-room", key = "#result"),
                    @CacheEvict(value = "reservation-by-id", key = "#reservationId")
            }
    )
    public Long deleteReservation(final Long reservationId) {
        final RoomReservationEntity roomReservationEntity = roomReservationRepository.findById(reservationId)
                .orElseThrow(() -> {
                    LOG.warn("Reservation not found during reservation deletion. ReservationId: {}", reservationId);
                    return new NotFoundException("Reservation not found during delete reservation");
                });

        final Long roomId = roomReservationEntity.getRoomId();
        LOG.info("Deleting reservation: {}", roomReservationEntity);
        roomReservationRepository.delete(roomReservationEntity);
        return roomId;
    }

    @Cacheable(value = "reservation-list-all", key = "'all'")
    public List<RoomReservationResponse> getAllReservation() {
        final List<RoomReservationEntity> roomReservationEntityList = roomReservationRepository.findAll();
        if (roomReservationEntityList.isEmpty()) {
            LOG.warn("No reservations found in database. Return empty list");
            return List.of();
        }

        LOG.info("Found {} reservations in database", roomReservationEntityList.size());
        return roomReservationEntityList.stream()
                .map(roomManagementMapper::mapRoomReservationEntityToRoomReservationResponse)
                .toList();
    }

    @Cacheable(value = "reservation-by-id", key = "#roomReservationId")
    public RoomReservationResponse getRoomReservationByReservationId(final Long roomReservationId) {
        final RoomReservationEntity roomReservationEntity = roomReservationRepository.findById(roomReservationId)
                .orElseThrow(() -> {
                    LOG.warn("Reservation not found during reservation retrieval. ReservationId: {}", roomReservationId);
                    return new NotFoundException("Reservation not found");
                });

        final RoomReservationResponse roomReservationResponse = roomManagementMapper.mapRoomReservationEntityToRoomReservationResponse(roomReservationEntity);
        LOG.info("Found reservation: {}", roomReservationResponse);
        return roomReservationResponse;
    }

    @Cacheable(value = "reservation-list-by-room", key = "#roomId")
    public List<RoomReservationResponse> getReservationsListByRoomId(final Long roomId) {
        final List<RoomReservationEntity> roomReservationEntityList =
                roomReservationRepository.findByRoomId(roomId);

        if (roomReservationEntityList.isEmpty()) {
            LOG.warn("No reservations found for room: {}. Return empty list", roomId);
            return List.of();
        }

        return roomReservationEntityList.stream()
                .map(roomManagementMapper::mapRoomReservationEntityToRoomReservationResponse)
                .toList();
    }
}
