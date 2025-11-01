package com.zakrzewski.reservationsystem.service;

import com.zakrzewski.reservationsystem.dto.request.EmployeeCreateAccountRequest;
import com.zakrzewski.reservationsystem.dto.response.EmployeeResponse;
import com.zakrzewski.reservationsystem.exceptions.ConflictException;
import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;
import com.zakrzewski.reservationsystem.exceptions.NotFoundException;
import com.zakrzewski.reservationsystem.mapper.EmployeeManagementMapper;
import com.zakrzewski.reservationsystem.model.EmployeeEntity;
import com.zakrzewski.reservationsystem.repository.EmployeeRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeManagementMapper employeeManagementMapper;
    private final EmployeeRepository employeeRepository;

    @SuppressWarnings("unused")
    public EmployeeService() {
        this(null, null);
    }

    @Autowired
    public EmployeeService(final EmployeeManagementMapper employeeManagementMapper,
                           final EmployeeRepository employeeRepository) {
        this.employeeManagementMapper = employeeManagementMapper;
        this.employeeRepository = employeeRepository;
    }

    @Caching(
            put = {
                    @CachePut(value = "employee", key = "#result.employeeId")
            },
            evict = {
                    @CacheEvict(value = "employee", key = "'all'"),
                    @CacheEvict(value = "employee", key = "#result.email"),
            }
    )
    @Transactional
    public EmployeeResponse createAccount(final EmployeeCreateAccountRequest employeeCreateAccountRequest) {
        final String employeeEmail = employeeCreateAccountRequest.email();
        LOG.info("Creating employee account for email: {}", employeeEmail);
        try {
            final EmployeeEntity employeeEntity = employeeManagementMapper.mapEmployeeCreateAccountRequestToEmployeeEntity(employeeCreateAccountRequest);
            final EmployeeEntity savedEmployee = employeeRepository.save(employeeEntity);
            final EmployeeResponse employeeResponse = employeeManagementMapper.mapEmployeeEntityToEmployeeResponse(savedEmployee);
            LOG.info("New employee has been saved successfully with id: {}", employeeResponse.employeeId());
            return employeeResponse;
        } catch (DataIntegrityViolationException dive) {
            LOG.error("DataIntegrityViolationException caught while saving employee. EmployeeCreateRequest: {} has duplicated fields, message: {}", employeeCreateAccountRequest, dive.getMessage(), dive);
            throw new ConflictException("Employee exist");
        }
    }

    @Cacheable(value = "employee", key = "'all'")
    public List<EmployeeResponse> getAllEmployees() {
        final List<EmployeeEntity> employeeList = employeeRepository.findAll();
        if (employeeList.isEmpty()) {
            LOG.warn("No employees found in database");
            return List.of();
        }

        LOG.info("Found {} employees in database", employeeList.size());
        return employeeList.stream()
                .map(employeeManagementMapper::mapEmployeeEntityToEmployeeResponse)
                .toList();
    }

    @Cacheable(value = "employee", key = "#employeeEmail")
    public EmployeeResponse getEmployeeByEmail(final String employeeEmail) {
        if (StringUtils.isBlank(employeeEmail)) {
            LOG.warn("Employee email is blank during get employee by email");
            throw new InvalidInputException("Employee email is blank");
        }
        final Optional<EmployeeEntity> employee = employeeRepository.findByEmail(employeeEmail);
        return employee.map(employeeManagementMapper::mapEmployeeEntityToEmployeeResponse)
                .orElseThrow(() -> {
                    LOG.warn("Employee: {} not found in database", employeeEmail);
                    return new NotFoundException("Employee not found");
                });
    }
}
