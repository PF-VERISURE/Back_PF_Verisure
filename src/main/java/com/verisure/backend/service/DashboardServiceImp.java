package com.verisure.backend.service;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.stereotype.Service;

import com.verisure.backend.dto.response.CategoryCountResponseDTO;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.ParticipationRecordRepository;
import com.verisure.backend.repository.ProjectRepository;

@Service
public class DashboardServiceImp implements DashboardService {

    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
    private final ParticipationRecordRepository participationRecordRepository;

    public DashboardServiceImp(ProjectRepository projectRepository, ApplicationRepository applicationRepository,
            ParticipationRecordRepository participationRecordRepository) {
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.participationRecordRepository = participationRecordRepository;
    }

    @Override
    public List<CategoryCountResponseDTO> getProjectsByCategory(Integer year, Integer month) {
        OffsetDateTime startDate;
        OffsetDateTime endDate;

        if (year != null) {
            if (month != null) {
                YearMonth yearMonth = YearMonth.of(year, month);
                startDate = yearMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
                endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
            } else {
                startDate = OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
                endDate = OffsetDateTime.of(year, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC);
            }
        } else {
            startDate = OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
            endDate = OffsetDateTime.of(2100, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC);
        }

        return projectRepository.countRegisteredProjectsByCategory(startDate, endDate);
    }

}