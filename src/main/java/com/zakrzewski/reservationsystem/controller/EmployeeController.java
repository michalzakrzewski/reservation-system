package com.zakrzewski.reservationsystem.controller;

import com.zakrzewski.reservationsystem.dto.response.EmployeeResponse;
import com.zakrzewski.reservationsystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(path = "/get-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        final List<EmployeeResponse> allEmployees = employeeService.getAllEmployees();
        return ResponseEntity.status(HttpStatus.OK).body(allEmployees);
    }

    @GetMapping(path = "/find-by-email/{employeeEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeResponse> getEmployeeByEmail(@PathVariable("employeeEmail") final String employeeEmail) {
        final EmployeeResponse employeeByEmail = employeeService.getEmployeeByEmail(employeeEmail);
        return ResponseEntity.status(HttpStatus.OK).body(employeeByEmail);
    }
}
