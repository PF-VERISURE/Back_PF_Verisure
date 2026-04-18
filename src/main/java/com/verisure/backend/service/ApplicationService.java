package com.verisure.backend.service;

import java.util.List;
import com.verisure.backend.dto.request.ApplicationRequestDTO;
import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;

public interface ApplicationService {

    EmployeeApplicationResponseDTO applyToProject(ApplicationRequestDTO request, String email);

    void cancelApplication(Long applicationId, Long userId);

    List<EmployeeApplicationResponseDTO> getMyApplications(Long userId);

    void completeApplication(Long applicationId);

}