package com.verisure.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verisure.backend.dto.response.SdgResponseDTO;
import com.verisure.backend.service.SdgService;

@RestController
@RequestMapping("api/v1/sdgs")
public class SdgController {

    private final SdgService sdgService;

    public SdgController(SdgService sdgService) {
        this.sdgService = sdgService;
    }

    @GetMapping
    public ResponseEntity<List<SdgResponseDTO>> getAllSdgs() {
        List<SdgResponseDTO> sdgs = sdgService.getAllSdg();
        return ResponseEntity.ok(sdgs);
    }
    
}
