package com.verisure.backend.service;

import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

        OffsetDateTime[] dateRange = calculateDateRange(year, month);
        List<Object[]> rawData = projectRepository.countProjectsByCategoryRaw(dateRange[0], dateRange[1]);
        return mapToCategoryCountDTOs(rawData);
    }

    @Override
    public List<CategoryCountResponseDTO> getApplicationsByCategory(Integer year, Integer month) {

        OffsetDateTime[] dateRange = calculateDateRange(year, month);
        List<Object[]> rawData = applicationRepository.countApplicationsByCategoryRaw(dateRange[0], dateRange[1]);
        return mapToCategoryCountDTOs(rawData);
    }


    private OffsetDateTime[] calculateDateRange(Integer year, Integer month) {

        if (year == null) {
            return new OffsetDateTime[] {
                    OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                    OffsetDateTime.of(2100, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC)
            };
        }

        if (month != null) {
            YearMonth yearMonth = YearMonth.of(year, month);
            return new OffsetDateTime[] {
                    yearMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC),
                    yearMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(ZoneOffset.UTC)
            };
        }

        return new OffsetDateTime[] {
                OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                OffsetDateTime.of(year, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC)
        };
    }

    private List<CategoryCountResponseDTO> mapToCategoryCountDTOs(List<Object[]> rawData) {
        List<CategoryCountResponseDTO> responseList = new ArrayList<>();

        for (Object[] row : rawData) {
            String categoryName = (String) row[0];
            Long count = ((Number) row[1]).longValue();

            responseList.add(new CategoryCountResponseDTO(categoryName, count));
        }

        return responseList;
    }

}