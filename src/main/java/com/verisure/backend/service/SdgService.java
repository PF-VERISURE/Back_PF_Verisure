package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.response.SdgResponseDTO;

public interface SdgService {

    SdgResponseDTO getById(Integer id);

    List<SdgResponseDTO> getAllSdgs();

}
