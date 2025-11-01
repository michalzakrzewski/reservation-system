package com.zakrzewski.reservationsystem.dto.request;

import com.zakrzewski.reservationsystem.enums.TeamEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder(setterPrefix = "with")
public record EmployeeCreateAccountRequest (
        @NotBlank(message = "Employee email can not be blank")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Employee password can not be blank")
        String password,

        @NotBlank(message = "First name can not be blank")
        String firstName,

        @NotBlank(message = "Last name can not be blank")
        String lastName,

        @NotNull(message = "Emplotee team can not be empty")
        TeamEnum team) {
}