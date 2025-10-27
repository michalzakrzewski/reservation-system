package com.zakrzewski.reservationsystem.dto.request;

import com.zakrzewski.reservationsystem.enums.TeamEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class EmployeeCreateAccountRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private TeamEnum team;
}
