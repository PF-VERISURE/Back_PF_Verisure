package com.verisure.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.verisure.backend.dto.response.GnoProfileResponseDTO;
import com.verisure.backend.service.GnoProfileService;

@RestController
@RequestMapping("api/v1/gnos/profile")
public class GnoProfileController {

    private final GnoProfileService gnoProfileService;

    public GnoProfileController(GnoProfileService gnoProfileService) {
        this.gnoProfileService = gnoProfileService;
    }

    @GetMapping
    public ResponseEntity<GnoProfileResponseDTO> getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        GnoProfileResponseDTO response = gnoProfileService.getMyProfile(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}