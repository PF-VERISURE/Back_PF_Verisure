package com.verisure.backend.controller;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.verisure.backend.dto.request.GnoCreateRequestDTO;
import com.verisure.backend.dto.response.GnoProfileResponseDTO;
import com.verisure.backend.service.GnoProfileService;

@RestController
@RequestMapping("/api/v1/gnos")
public class GnoProfileController {

    private final GnoProfileService gnoProfileService;

    public GnoProfileController(GnoProfileService gnoProfileService) {
        this.gnoProfileService = gnoProfileService;
    }

    @GetMapping("/profile")
    public ResponseEntity<GnoProfileResponseDTO> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        GnoProfileResponseDTO response = gnoProfileService.getMyProfile(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GnoProfileResponseDTO>> getAllGnos() {
        List<GnoProfileResponseDTO> response = gnoProfileService.getAllGnoProfiles();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GnoProfileResponseDTO> getGnoById(@PathVariable Long id) {
        GnoProfileResponseDTO response = gnoProfileService.getGnoProfile(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GnoProfileResponseDTO> createGno(@Valid @RequestBody GnoCreateRequestDTO payload) {
        GnoProfileResponseDTO response = gnoProfileService.createGnoProfile(payload);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGno(@PathVariable Long id) {
        gnoProfileService.deleteGno(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}