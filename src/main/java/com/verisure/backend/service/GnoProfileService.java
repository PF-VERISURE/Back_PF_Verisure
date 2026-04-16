package com.verisure.backend.service;

import java.util.List;
import com.verisure.backend.dto.request.GnoCreateRequestDTO;
import com.verisure.backend.dto.response.GnoProfileResponseDTO;

public interface GnoProfileService {

    GnoProfileResponseDTO createGnoProfile(GnoCreateRequestDTO request);

    GnoProfileResponseDTO getMyProfile(String email);
    
    GnoProfileResponseDTO getGnoProfile(Long id);
    
    List<GnoProfileResponseDTO> getAllGnoProfiles();

    void deleteGno(Long id);

}