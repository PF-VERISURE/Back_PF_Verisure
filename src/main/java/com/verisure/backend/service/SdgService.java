package com.verisure.backend.service;

import com.verisure.backend.dto.response.SdgResponseDTO;
import java.util.List;

public interface SdgService {
    List<SdgResponseDTO> getAllSdg();
}
