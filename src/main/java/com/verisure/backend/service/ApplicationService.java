package com.verisure.backend.service;

import com.verisure.backend.dto.request.ApplicationRequestDTO;
import com.verisure.backend.dto.response.AdminApplicationListResponseDTO;
import com.verisure.backend.dto.response.EmployeeApplicationListResponseDTO;
import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;

public interface ApplicationService {

    // ADMIN:
    AdminApplicationListResponseDTO getAllApplications();

    // EMPLOYEE:
    EmployeeApplicationResponseDTO applyToProject(ApplicationRequestDTO request, Long userId);

    void cancelApplication(Long applicationId, Long userId);

    EmployeeApplicationListResponseDTO getMyApplications(Long userId);

    void completeApplication(Long applicationId);

}

