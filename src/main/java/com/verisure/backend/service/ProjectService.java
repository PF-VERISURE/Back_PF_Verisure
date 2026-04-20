package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectListResponseDTO;//Refactor Alex feedback
import com.verisure.backend.dto.response.ProjectResponseDTO;

public interface ProjectService {
    
    //------ONG------/
    ProjectResponseDTO createProject(ProjectRequestDTO dto, Long userId);
    ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long id, Long userId);
    void deleteProject(Long id, Long userId);
    List<ProjectResponseDTO> getMyProjects(Long userId);
    //ProjectListResponseDTO getMyProjects(Long userId);//Refactor Alex feedback

    //---------ADMIN-----/
    List<ProjectResponseDTO> getPendingProjects();
    //ProjectListResponseDTO getPendingProjects();//Refactor Alex feedback
    List<ProjectResponseDTO> getAllProjectsForAdmin();
    //ProjectListResponseDTO getAllProjectsForAdmin();//Refactor Alex feedback

    //ProjectResponseDTO updateStatus(Long projectId, String status);

}