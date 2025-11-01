package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.dto.request.EmployeeCreateAccountRequest;
import com.zakrzewski.reservationsystem.dto.response.EmployeeResponse;
import com.zakrzewski.reservationsystem.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    @SuppressWarnings("unused")
    public EmployeeController() {
        this(null);
    }

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(path = "/create-account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> createAccount(@RequestBody @Valid final EmployeeCreateAccountRequest employeeCreateAccountRequest) {
        final EmployeeResponse employeeResponse = employeeService.createAccount(employeeCreateAccountRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeResponse);
    }

    @GetMapping(path = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        final List<EmployeeResponse> allEmployees = employeeService.getAllEmployees();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allEmployees);
    }

    @GetMapping(path = "/find-by-email/{employeeEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> getEmployeeByEmail(@PathVariable("employeeEmail") final String employeeEmail) {
        final EmployeeResponse employeeByEmail = employeeService.getEmployeeByEmail(employeeEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeByEmail);
    }
}
