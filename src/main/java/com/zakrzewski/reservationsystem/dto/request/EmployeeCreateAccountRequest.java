package com.zakrzewski.reservationsystem.dto.request;

import com.zakrzewski.reservationsystem.enums.TeamEnum;
import lombok.*;

@Builder(setterPrefix = "with")
public record EmployeeCreateAccountRequest (
        String email,
        String password,
        String firstName,
        String lastName,
        TeamEnum team) {
}
