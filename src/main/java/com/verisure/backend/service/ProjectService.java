package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;

public interface ProjectService {
    
    //------ONG------/
    ProjectResponseDTO createProject(ProjectRequestDTO dto, String email);
    ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long id, String email);
    void deleteProject(Long id, String email);
    List<ProjectResponseDTO> getMyProjects(String email);

    //---------ADMIN-----/
    List<ProjectResponseDTO> getPendingProjects();
    List<ProjectResponseDTO> getAllProjectsForAdmin();
    //ProjectResponseDTO updateStatus(Long projectId, String status);

}
