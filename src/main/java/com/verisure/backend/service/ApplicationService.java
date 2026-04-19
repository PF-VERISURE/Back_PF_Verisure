package com.verisure.backend.service;

import java.util.List;
import com.verisure.backend.dto.request.ApplicationRequestDTO;
import com.verisure.backend.dto.response.AdminApplicationResponseDTO;
import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;

public interface ApplicationService {

    // ADMIN:
    List<AdminApplicationResponseDTO> getAllApplications();

    // EMPLOYEE:
    EmployeeApplicationResponseDTO applyToProject(ApplicationRequestDTO request, Long userId);

    void cancelApplication(Long applicationId, Long userId);

    List<EmployeeApplicationResponseDTO> getMyApplications(Long userId);



    // Sistema automatizacion.
    void completeApplication(Long applicationId);

}