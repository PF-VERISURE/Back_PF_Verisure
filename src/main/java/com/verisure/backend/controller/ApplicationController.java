package com.verisure.backend.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.verisure.backend.dto.request.ApplicationRequestDTO;
import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;
import com.verisure.backend.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<EmployeeApplicationResponseDTO> applyToProject(
            @Valid @RequestBody ApplicationRequestDTO request,
            Authentication authentication) {

        String email = authentication.getUsername();

        EmployeeApplicationResponseDTO response = applicationService.applyToProject(request, email);
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}