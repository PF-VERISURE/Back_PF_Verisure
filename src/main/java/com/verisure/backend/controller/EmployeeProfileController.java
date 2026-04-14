package com.verisure.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verisure.backend.dto.response.EmployeeProfileResponseDTO;
import com.verisure.backend.service.EmployeeProfileService;

@RestController
@RequestMapping("/api/v1/employees/profile")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    public EmployeeProfileController(EmployeeProfileService employeeProfileService) {
        this.employeeProfileService = employeeProfileService;
    }

    @GetMapping
    public ResponseEntity<EmployeeProfileResponseDTO> getMyProfile(Authentication authentication) {
        String email = authentication.getName();

        EmployeeProfileResponseDTO response = employeeProfileService.getMyProfile(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}