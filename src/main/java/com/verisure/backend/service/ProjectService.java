package com.verisure.backend.service;

//import java.util.List;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;

public interface ProjectService {
    
    //------ONG------/
    ProjectResponseDTO createProject(ProjectRequestDTO dto, String email);
    //ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long projectId);
    //void deleteProject(Long projectId);
    //List<ProjectResponseDTO> getMyProjectsByGno(Long gnoId);
}
