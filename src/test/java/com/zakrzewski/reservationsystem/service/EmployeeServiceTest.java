package com.zakrzewski.reservationsystem.service;

import com.zakrzewski.reservationsystem.dto.request.EmployeeCreateAccountRequest;
import com.zakrzewski.reservationsystem.dto.response.EmployeeResponse;
import com.zakrzewski.reservationsystem.enums.TeamEnum;
import com.zakrzewski.reservationsystem.exceptions.ConflictException;
import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;
import com.zakrzewski.reservationsystem.exceptions.NotFoundException;
import com.zakrzewski.reservationsystem.mapper.EmployeeManagementMapper;
import com.zakrzewski.reservationsystem.model.EmployeeEntity;
import com.zakrzewski.reservationsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeManagementMapper employeeManagementMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeCreateAccountRequest createRequest;
    private EmployeeEntity entity;
    private EmployeeResponse response;

    @BeforeEach
    void setUp() {
        final String TEST_EMAIL = "test@example.com";
        createRequest = new EmployeeCreateAccountRequest(TEST_EMAIL, "pass", "Jan", "Kowalski", TeamEnum.HUMAN_RESOURCES);
        entity = new EmployeeEntity();
        entity.setEmployeeId(1L);
        entity.setEmail(TEST_EMAIL);
        response = new EmployeeResponse(1L, "Jan", "Kowalski", TEST_EMAIL, TeamEnum.HUMAN_RESOURCES);
    }

    @Test
    void createAccount_Success() {
        //GIVEN
        when(employeeManagementMapper.mapEmployeeCreateAccountRequestToEmployeeEntity(createRequest)).thenReturn(entity);
        when(employeeRepository.save(entity)).thenReturn(entity);
        when(employeeManagementMapper.mapEmployeeEntityToEmployeeResponse(entity)).thenReturn(response);

        //WHEN
        final EmployeeResponse result = employeeService.createAccount(createRequest);

        // THEN
        assertNotNull(result);
        assertEquals(response.employeeId(), result.employeeId());

        verify(employeeRepository, times(1)).save(entity);
        verify(employeeManagementMapper, times(1)).mapEmployeeEntityToEmployeeResponse(entity);
    }

    @Test
    void createAccount_ThrowsConflictException_OnDuplicatedEmail() {
        // GIVEN
        when(employeeManagementMapper.mapEmployeeCreateAccountRequestToEmployeeEntity(any())).thenReturn(entity);
        when(employeeRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        // WHEN / THEN
        assertThrows(ConflictException.class, () -> employeeService.createAccount(createRequest));
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void createAccount_ThrowsConflictException_OnOtherDataIntegrityViolation() {
        // GIVEN
        when(employeeManagementMapper.mapEmployeeCreateAccountRequestToEmployeeEntity(createRequest)).thenReturn(entity);
        when(employeeRepository.save(entity)).thenThrow(DataIntegrityViolationException.class);

        // WHEN / THEN
        assertThrows(ConflictException.class, () -> employeeService.createAccount(createRequest));
        verify(employeeRepository, times(1)).save(entity);
        verify(employeeManagementMapper, never()).mapEmployeeEntityToEmployeeResponse(any());
    }

    @Test
    void getEmployeeByEmail_Success() {
        // GIVEN
        final String email = "test@example.com";
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(entity));
        when(employeeManagementMapper.mapEmployeeEntityToEmployeeResponse(entity)).thenReturn(response);

        // WHEN
        EmployeeResponse result = employeeService.getEmployeeByEmail(email);

        // THEN
        assertNotNull(result);
        assertEquals(response.email(), result.email());
        verify(employeeManagementMapper, times(1)).mapEmployeeEntityToEmployeeResponse(entity);
    }

    @Test
    void getEmployeeByEmail_ThrowsNotFoundException_WhenEmployeeDoesNotExist() {
        // GIVEN
        final String email = "nonexistent@example.com";
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());

        // WHEN / THEN
        assertThrows(NotFoundException.class, () -> employeeService.getEmployeeByEmail(email));
        verify(employeeManagementMapper, never()).mapEmployeeEntityToEmployeeResponse(any());
    }

    @Test
    void getEmployeeByEmail_ThrowsInvalidInputException_WhenEmailIsBlank() {
        // GIVEN
        final String blankEmail = " ";

        // WHEN / THEN
        assertThrows(InvalidInputException.class, () -> employeeService.getEmployeeByEmail(blankEmail));
        verify(employeeRepository, never()).findByEmail(anyString());
    }
}