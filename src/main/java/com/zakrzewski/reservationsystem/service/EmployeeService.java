package com.zakrzewski.reservationsystem.service;

import com.zakrzewski.reservationsystem.dto.request.EmployeeCreateAccountRequest;
import com.zakrzewski.reservationsystem.dto.response.EmployeeResponse;
import com.zakrzewski.reservationsystem.exceptions.ConflictException;
import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;
import com.zakrzewski.reservationsystem.exceptions.NotFoundException;
import com.zakrzewski.reservationsystem.mapper.EmployeeMapper;
import com.zakrzewski.reservationsystem.model.EmployeeEntity;
import com.zakrzewski.reservationsystem.repository.EmployeeRepository;
import com.zakrzewski.reservationsystem.validator.EmployeeValidator;
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
public class EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;
    private final EmployeeRepository employeeRepository;

    @SuppressWarnings("unused")
    public EmployeeService() {
        this(null, null, null);
    }

    @Autowired
    public EmployeeService(final EmployeeMapper employeeMapper,
                           final EmployeeValidator employeeValidator,
                           final EmployeeRepository employeeRepository) {
        this.employeeMapper = employeeMapper;
        this.employeeValidator = employeeValidator;
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
    public EmployeeResponse createAccount(final EmployeeCreateAccountRequest employeeCreateAccountRequest) {
        employeeValidator.validateEmployeeCreateAccountRequest(employeeCreateAccountRequest);

        final String employeeEmail = employeeCreateAccountRequest.getEmail();
        if (employeeRepository.existsByEmail(employeeEmail)) {
            LOG.warn("Employee email already exists");
            throw new ConflictException("Employee email already exists");
        }

        LOG.info("Creating employee account for email: {}", employeeEmail);
        try {
            final EmployeeEntity employeeEntity = employeeMapper.mapEmployeeCreateAccountRequestToEmployeeEntity(employeeCreateAccountRequest);
            final EmployeeEntity savedEmployee = employeeRepository.save(employeeEntity);
            final EmployeeResponse employeeResponse = employeeMapper.mapEmployeeEntityToEmployeeResponse(savedEmployee);
            LOG.info("New employee has been saved successfully with id: {}", employeeResponse.getEmployeeId());
            return employeeResponse;
        } catch (DataIntegrityViolationException dive) {
            LOG.error("DataIntegrityViolationException caught while saving employee with email {}. Possible constraint violation, message: {}", employeeEmail, dive.getMessage(), dive);
            throw new ConflictException("Constraint violation while creating employee");
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
                .map(employeeMapper::mapEmployeeEntityToEmployeeResponse)
                .toList();
    }

    @Cacheable(value = "employee", key = "#employeeEmail")
    public EmployeeResponse getEmployeeByEmail(final String employeeEmail) {
        if (StringUtils.isBlank(employeeEmail)) {
            LOG.error("Employee email is blank");
            throw new InvalidInputException("Employee email is blank");
        }
        final Optional<EmployeeEntity> employee = employeeRepository.findByEmail(employeeEmail);
        return employee.map(employeeMapper::mapEmployeeEntityToEmployeeResponse)
                .orElseThrow(() -> new NotFoundException("Employee not found"));
    }
}
