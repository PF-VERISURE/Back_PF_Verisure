package com.verisure.backend.controller;

import java.util.List;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;
import com.verisure.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(
            @Valid @RequestBody
            ProjectRequestDTO dto, 
            Authentication authentication) {
        String email = authentication.getName();
        ProjectResponseDTO response = projectService.createProject(dto, email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(
            @PathVariable Long id, 
            @Valid @RequestBody ProjectRequestDTO dto, 
            Authentication authentication) {

        String email = authentication.getName();
        ProjectResponseDTO response = projectService.updateProject(dto, id, email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id, 
            Authentication authentication) {
        String email = authentication.getName();
        projectService.deleteProject(id, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<List<ProjectResponseDTO>> getMyProjects(Authentication authentication) {
        String email = authentication.getName();
        List<ProjectResponseDTO> response = projectService.getMyProjects(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ProjectResponseDTO>> getPendingProjects() {
        List<ProjectResponseDTO> pendingProjects = projectService.getPendingProjects();
        return new ResponseEntity<>(pendingProjects, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectResponseDTO>> getAllProjectsForAdmin(Authentication authentication) {
        List<ProjectResponseDTO> response = projectService.getAllProjectsForAdmin();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
