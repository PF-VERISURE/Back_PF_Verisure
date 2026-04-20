package com.verisure.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.verisure.backend.dto.request.ApplicationRequestDTO;
import com.verisure.backend.dto.response.AdminApplicationListResponseDTO;
import com.verisure.backend.dto.response.AdminApplicationResponseDTO;
import com.verisure.backend.dto.response.EmployeeApplicationListResponseDTO;
import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;
import com.verisure.backend.security.AuthenticatedUser;
import org.springframework.security.core.Authentication;
import com.verisure.backend.service.ApplicationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // ADMIN endpoints.
    
    @GetMapping("/all")
    public ResponseEntity<AdminApplicationListResponseDTO> getAllApplications(Authentication authentication) {
        AdminApplicationListResponseDTO response = applicationService.getAllApplications();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }  
    
    // EMPLOYEE endpoints.

    @PostMapping
    public ResponseEntity<EmployeeApplicationResponseDTO> applyToProject(@Valid @RequestBody ApplicationRequestDTO request,
            Authentication authentication) {

        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();
        EmployeeApplicationResponseDTO response = applicationService.applyToProject(request, currentUser.userId());
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelApplication(@PathVariable Long id, Authentication authentication) {
            
        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();
        applicationService.cancelApplication(id, currentUser.userId());
        
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeApplicationListResponseDTO> getMyApplications(Authentication authentication) {
        
        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();
        EmployeeApplicationListResponseDTO response = applicationService.getMyApplications(currentUser.userId());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}