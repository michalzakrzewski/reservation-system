package com.zakrzewski.reservationsystem.service;

import com.zakrzewski.reservationsystem.dto.response.EmployeeResponse;
import com.zakrzewski.reservationsystem.exceptions.InvalidInputException;
import com.zakrzewski.reservationsystem.exceptions.NotFoundException;
import com.zakrzewski.reservationsystem.mapper.EmployeeMapper;
import com.zakrzewski.reservationsystem.model.EmployeeEntity;
import com.zakrzewski.reservationsystem.repository.EmployeeRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    @SuppressWarnings("unused")
    public EmployeeService() {
        this(null, null);
    }

    @Autowired
    public EmployeeService(final EmployeeMapper employeeMapper,
                           final EmployeeRepository employeeRepository) {
        this.employeeMapper = employeeMapper;
        this.employeeRepository = employeeRepository;
    }

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
