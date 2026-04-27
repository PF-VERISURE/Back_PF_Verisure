package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.response.CertificateResponseDTO;
import com.verisure.backend.entity.Application;

public interface ParticipationRecordService {

    void createParticipationRecord(Application application);

    CertificateResponseDTO getCertificateById(Long participationId, Long userId);

    List<CertificateResponseDTO> getMyCertificates(Long userId);

}