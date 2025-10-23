package com.zakrzewski.reservationsystem.mapper;

import com.zakrzewski.reservationsystem.dto.response.EmployeeResponse;
import com.zakrzewski.reservationsystem.model.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeMapper() {
    }

    public EmployeeResponse mapEmployeeEntityToEmployeeResponse(final EmployeeEntity employeeEntity) {
        return EmployeeResponse.builder()
                .withEmployeeId(employeeEntity.getEmployeeId())
                .withFirstName(employeeEntity.getFirstName())
                .withLastName(employeeEntity.getLastName())
                .withEmail(employeeEntity.getEmail())
                .withTeam(employeeEntity.getTeam())
                .build();
    }
}
