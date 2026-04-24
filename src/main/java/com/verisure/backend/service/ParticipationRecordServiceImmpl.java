package com.verisure.backend.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.ParticipationRecord;
import com.verisure.backend.repository.ParticipationRecordRepository;

@Service
public class ParticipationRecordServiceImmpl implements ParticipationRecordService {

    private final ParticipationRecordRepository participationRecordRepository;

    public ParticipationRecordServiceImmpl(ParticipationRecordRepository participationRecordRepository) {
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
}