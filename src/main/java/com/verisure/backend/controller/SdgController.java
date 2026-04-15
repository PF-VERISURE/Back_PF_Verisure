package com.verisure.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verisure.backend.service.SdgService;
import com.verisure.backend.dto.response.SdgResponseDTO;

@RestController
@RequestMapping("/api/v1/sdgs")
public class SdgController {

    private final SdgService sdgService;

    public SdgController(SdgService sdgService) {
        this.sdgService = sdgService;
    }

    @GetMapping
    public List<SdgResponseDTO> getAllSdgs() {
        return sdgService.getAllSdgs();
    }

    @GetMapping("/{id}")
    public SdgResponseDTO getSdgById(@PathVariable Integer id) {
        return sdgService.getById(id);
    }
    
}