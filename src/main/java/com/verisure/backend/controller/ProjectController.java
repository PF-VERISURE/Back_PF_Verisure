package com.verisure.backend.controller;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectListResponseDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;
import com.verisure.backend.security.AuthenticatedUser;
import com.verisure.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestPart("project")
            ProjectRequestDTO dto, 
            @RequestPart(value = "file", required = false) MultipartFile image,
            Authentication authentication) {
        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();
        ProjectResponseDTO response = projectService.createProject(dto, currentUser.userId(), image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequestDTO dto,
            Authentication authentication) {

        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();
        ProjectResponseDTO response = projectService.updateProject(dto, id, currentUser.userId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id,
            Authentication authentication) {
        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();
        projectService.deleteProject(id, currentUser.userId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<ProjectListResponseDTO> getMyProjects(
            Authentication authentication) {
        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();
        ProjectListResponseDTO response = projectService.getMyProjects(currentUser.userId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<ProjectListResponseDTO> getPendingProjects() {
        ProjectListResponseDTO pendingProjects = projectService.getPendingProjects();
        return new ResponseEntity<>(pendingProjects, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ProjectListResponseDTO> getAllProjectsForAdmin(Authentication authentication) {
        ProjectListResponseDTO response = projectService.getAllProjectsForAdmin();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

