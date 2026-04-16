package com.verisure.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verisure.backend.dto.response.EmployeeProfileResponseDTO;
import com.verisure.backend.service.EmployeeProfileService;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    public EmployeeProfileController(EmployeeProfileService employeeProfileService) {
        this.employeeProfileService = employeeProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<EmployeeProfileResponseDTO> getMyProfile(Authentication authentication) {
        String email = authentication.getName();

        EmployeeProfileResponseDTO response = employeeProfileService.getMyProfile(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeProfileResponseDTO>> getAllEmployees() {
        List<EmployeeProfileResponseDTO> response = employeeProfileService.getAllEmployeeProfiles();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfileResponseDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeProfileResponseDTO response = employeeProfileService.getEmployeeProfile(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}