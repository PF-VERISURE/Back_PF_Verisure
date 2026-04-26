package com.verisure.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.verisure.backend.dto.response.SdgListResponseDTO;
import com.verisure.backend.dto.response.SdgResponseDTO;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.mapper.SdgMapper;
import com.verisure.backend.repository.SdgRepository;

@Service
public class SdgServiceImpl implements SdgService {

    private final SdgRepository sdgRepository;
    private final SdgMapper sdgMapper;

    public SdgServiceImpl(SdgRepository sdgRepository, SdgMapper sdgMapper) {
        this.sdgRepository = sdgRepository;
        this.sdgMapper = sdgMapper;
    }   

    @Override
    @Transactional(readOnly = true)
    public SdgResponseDTO getById(Integer id) {
        return sdgRepository.findById(id)
                .map(sdgMapper::toSdgResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el ODS con el ID: " + id));
    }
    @Override
    @Transactional(readOnly = true)
    public SdgListResponseDTO getAllSdgs() {
        List<Sdg> sdgs = sdgRepository.findAll();
        List<SdgResponseDTO> sdgsList = sdgMapper.toSdgListResponse(sdgs);
        return new SdgListResponseDTO(sdgsList, sdgsList.size());
    }

}