package com.verisure.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verisure.backend.dto.response.CertificateResponseDTO;
import com.verisure.backend.security.AuthenticatedUser;
import com.verisure.backend.service.ParticipationRecordService;

@RestController
@RequestMapping("/api/v1/participation")
public class ParticipationRecordController {

    private final ParticipationRecordService participationRecordService;

    public ParticipationRecordController(ParticipationRecordService participationRecordService) {
        this.participationRecordService = participationRecordService;
    }

    @GetMapping("/certificate/{id}")
    public ResponseEntity<CertificateResponseDTO> getCertificateById(@PathVariable Long id, Authentication authentication) {
        AuthenticatedUser currentUser = (AuthenticatedUser) authentication.getPrincipal();
        CertificateResponseDTO response = participationRecordService.getCertificateById(id, currentUser.userId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/certificates")
    public ResponseEntity<List<CertificateResponseDTO>> getMyCertificates(Authentication Authentication) {
        AuthenticatedUser currentUser = (AuthenticatedUser) Authentication.getPrincipal();
        List<CertificateResponseDTO> response = participationRecordService.getMyCertificates(currentUser.userId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}