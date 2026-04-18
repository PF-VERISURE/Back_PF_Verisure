package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;

public interface ProjectService {
    
    //------ONG------/
    ProjectResponseDTO createProject(ProjectRequestDTO dto, Long userId);
    ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long id, Long userId);
    void deleteProject(Long id, Long userId);
    List<ProjectResponseDTO> getMyProjects(Long userId);

    //---------ADMIN-----/
    List<ProjectResponseDTO> getPendingProjects();
    List<ProjectResponseDTO> getAllProjectsForAdmin();
    //ProjectResponseDTO updateStatus(Long projectId, String status);

}