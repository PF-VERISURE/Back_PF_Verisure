package com.verisure.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.request.StatusUpdateRequestDTO;
import com.verisure.backend.dto.response.ProjectListResponseDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;

@Service
public interface ProjectService {
    
    //------ONG------/
    ProjectResponseDTO createProject(ProjectRequestDTO dto, Long userId, MultipartFile image);
    ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long id, Long userId, MultipartFile image);
    void deleteProject(Long id, Long userId);
    ProjectListResponseDTO getMyProjects(Long userId);

    //---------ADMIN-----/
    ProjectListResponseDTO getPendingProjects();
    ProjectListResponseDTO getAllProjectsForAdmin();
    ProjectResponseDTO updateStatus(Long id, StatusUpdateRequestDTO statusDto);

    //-----EMPLOYEED-----/
    ProjectListResponseDTO getAllPublished();
}