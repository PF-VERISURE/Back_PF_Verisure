package com.verisure.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.verisure.backend.service.SdgService;
import com.verisure.backend.dto.response.SdgListResponseDTO;
import com.verisure.backend.dto.response.SdgResponseDTO;

@RestController
@RequestMapping("/api/v1/sdgs")
public class SdgController {

    private final SdgService sdgService;

    public SdgController(SdgService sdgService) {
        this.sdgService = sdgService;
    }

    @GetMapping
    public ResponseEntity<SdgListResponseDTO> getAllSdgs() {
        SdgListResponseDTO response = sdgService.getAllSdgs();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SdgResponseDTO> getSdgById(@PathVariable Integer id) {
        SdgResponseDTO response = sdgService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}