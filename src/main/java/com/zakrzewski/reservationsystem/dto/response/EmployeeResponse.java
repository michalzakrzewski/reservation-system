package com.zakrzewski.reservationsystem.dto.response;

import com.zakrzewski.reservationsystem.enums.TeamEnum;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record EmployeeResponse (
        Long employeeId,
        String firstName,
        String lastName,
        String email,
        TeamEnum team) {
}
