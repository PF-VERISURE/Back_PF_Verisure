package com.verisure.backend.service;

import com.verisure.backend.dto.response.SdgListResponseDTO;
import com.verisure.backend.dto.response.SdgResponseDTO;

public interface SdgService {

    SdgResponseDTO getById(Integer id);

    SdgListResponseDTO getAllSdgs();

}
