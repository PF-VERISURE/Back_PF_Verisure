package com.verisure.backend.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.verisure.backend.dto.response.CertificateResponseDTO;
import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.ParticipationRecord;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.repository.ParticipationRecordRepository;

@Service
public class ParticipationRecordServiceImpl implements ParticipationRecordService {

    private final ParticipationRecordRepository participationRecordRepository;

    public ParticipationRecordServiceImpl(ParticipationRecordRepository participationRecordRepository) {
        this.participationRecordRepository = participationRecordRepository;
    }

    @Override
    public void createParticipationRecord(Application application) {

        ParticipationRecord participationRecord = new ParticipationRecord();
        participationRecord.setApplication(application);
        BigDecimal totalHours = BigDecimal.valueOf(application.getProject().getTotalHours());
        participationRecord.setLoggedHours(totalHours);
        participationRecord.setImpactMetric(BigDecimal.ZERO);

        participationRecordRepository.save(participationRecord);
    }

    @Override
    public CertificateResponseDTO getCertificateById(Long participationId, Long userId) {
        List<Object[]> rawData = participationRecordRepository.findRawCertificateById(participationId, userId);
        List<CertificateResponseDTO> certificates = mapRawDataToCertificates(rawData);
        if (certificates.isEmpty()) {
            throw new ResourceNotFoundException("Certificado no encontrado o no pertenece al usuario");
        }
        return certificates.get(0);
    }

    @Override
    public List<CertificateResponseDTO> getMyCertificates(Long userId) {
        List<Object[]> rawData = participationRecordRepository.findRawCertificatesByUserId(userId);
        return mapRawDataToCertificates(rawData);
    }

    
    private List<CertificateResponseDTO> mapRawDataToCertificates(List<Object[]> rawData) {
        Map<Long, CertificateResponseDTO> certificateMap = new LinkedHashMap<>();

        for (Object[] row : rawData) {
            Long participationId = ((Number) row[0]).longValue();
            String sdgName = (String) row[7];

            if (certificateMap.containsKey(participationId)) {
                if (sdgName != null) {
                    certificateMap.get(participationId).sdgs().add(sdgName);
                }
                continue;
            }

            String fullName = (String) row[1] + " " + (String) row[2];
            String projectTitle = (String) row[3];
            String gnoName = (String) row[4];
            BigDecimal loggedHours = (BigDecimal) row[5];
            OffsetDateTime endDate = (OffsetDateTime) row[6];

            List<String> sdgList = new ArrayList<>();
            if (sdgName != null) {
                sdgList.add(sdgName);
            }

            CertificateResponseDTO dto = new CertificateResponseDTO(
                    participationId,
                    fullName,
                    projectTitle,
                    sdgList,
                    gnoName,
                    loggedHours,
                    endDate);

            certificateMap.put(participationId, dto);
        }

        return new ArrayList<>(certificateMap.values());
    }
}