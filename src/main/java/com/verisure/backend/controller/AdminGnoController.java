package com.verisure.backend.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.verisure.backend.dto.request.GnoCreateRequestDTO;
import com.verisure.backend.dto.response.GnoProfileResponseDTO;
import com.verisure.backend.service.GnoProfileService;

@RestController
@RequestMapping("/api/v1/admin/gnos")
public class AdminGnoController {

    private final GnoProfileService gnoProfileService;

    public AdminGnoController(GnoProfileService gnoProfileService) {
        this.gnoProfileService = gnoProfileService;
    }

    @PostMapping
    public ResponseEntity<GnoProfileResponseDTO> createGno(@Valid @RequestBody GnoCreateRequestDTO payload) {
        GnoProfileResponseDTO response = gnoProfileService.createGnoProfile(payload);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GnoProfileResponseDTO> getGnoById(@PathVariable Long id) {
        GnoProfileResponseDTO response = gnoProfileService.getGnoProfile(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GnoProfileResponseDTO>> getAllGnos() {
        List<GnoProfileResponseDTO> response = gnoProfileService.getAllGnoProfiles();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGno(@PathVariable Long id) {
        gnoProfileService.deleteGno(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}