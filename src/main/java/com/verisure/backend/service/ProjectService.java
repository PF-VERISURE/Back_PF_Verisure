package com.verisure.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.request.StatusUpdateRequestDTO;
import com.verisure.backend.dto.response.ProjectListResponseDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;
import com.verisure.backend.entity.enums.LocationType;

@Service
public interface ProjectService {
    
    ProjectResponseDTO createProject(ProjectRequestDTO dto, Long userId, MultipartFile image);

    ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long id, Long userId, MultipartFile image);

    void deleteProject(Long id, Long userId);

    ProjectListResponseDTO getMyProjects(Long userId);

    ProjectListResponseDTO getPendingProjects();

    ProjectListResponseDTO getAllProjectsForAdmin();

    ProjectResponseDTO updateStatus(Long id, StatusUpdateRequestDTO statusDto); 

    ProjectListResponseDTO getAllPublished(String city, LocationType locationType, String title);

    void toggleFavorite(Long id, Long userId);

}