package com.verisure.backend.service;

import com.verisure.backend.dto.response.EmployeeProfileListResponseDTO;
import com.verisure.backend.dto.response.EmployeeProfileResponseDTO;

public interface EmployeeProfileService {

    EmployeeProfileResponseDTO getEmployeeProfile(Long id);

    EmployeeProfileListResponseDTO getAllEmployeeProfiles();
    
    EmployeeProfileResponseDTO getMyProfile(Long userId); 
    
}