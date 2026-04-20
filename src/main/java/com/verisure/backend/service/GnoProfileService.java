package com.verisure.backend.service;

import com.verisure.backend.dto.request.GnoCreateRequestDTO;
import com.verisure.backend.dto.response.GnoProfileListResponseDTO;
import com.verisure.backend.dto.response.GnoProfileResponseDTO;

public interface GnoProfileService {

    GnoProfileResponseDTO createGnoProfile(GnoCreateRequestDTO request);

    GnoProfileResponseDTO getMyProfile(Long userId);
    
    GnoProfileResponseDTO getGnoProfile(Long id);
    
    GnoProfileListResponseDTO getAllGnoProfiles();

    void deleteGno(Long id);

}