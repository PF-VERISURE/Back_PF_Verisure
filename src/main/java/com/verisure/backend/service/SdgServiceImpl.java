package com.verisure.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.verisure.backend.dto.response.SdgResponseDTO;
import com.verisure.backend.repository.SdgRepository;

@Service
public class SdgServiceImpl implements SdgService{
    
    private final SdgRepository sdgRepository;

    public SdgServiceImpl(SdgRepository sdgRepository) {
        this.sdgRepository = sdgRepository;
    }

    @Override
    public List<SdgResponseDTO> getAllSdg() {
        return sdgRepository.findAll().stream()
                .map(sdg -> new SdgResponseDTO(
                        sdg.getId(),
                        sdg.getName()
                ))
                .collect(Collectors.toList());
    }
}
