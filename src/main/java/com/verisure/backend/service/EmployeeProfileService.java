package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.response.EmployeeProfileResponseDTO;

public interface EmployeeProfileService {

    EmployeeProfileResponseDTO getEmployeeProfile(Long id);

    List<EmployeeProfileResponseDTO> getAllEmployeeProfiles();
    
    EmployeeProfileResponseDTO getMyProfile(String email); 
    
}