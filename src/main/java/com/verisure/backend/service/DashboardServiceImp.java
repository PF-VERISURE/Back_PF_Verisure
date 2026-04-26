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


    private List<CategoryCountResponseDTO> mapToCategoryCountDTOs(List<Object[]> rawData) {
        List<CategoryCountResponseDTO> responseList = new ArrayList<>();

        for (Object[] row : rawData) {
            String categoryName = (String) row[0];
            Long count = ((Number) row[1]).longValue();

            responseList.add(new CategoryCountResponseDTO(categoryName, count));
        }

        return responseList;
    }

    private OffsetDateTime[] calculateDateRange(Integer year, Integer month) {
        
        OffsetDateTime startDate;
        OffsetDateTime endDate;

        if (year == null) {
            startDate = getHistoricalStartDate();
            endDate = getHistoricalEndDate();
        } else if (month != null) {
            startDate = getMonthStartDate(year, month);
            endDate = getMonthEndDate(year, month);
        } else {
            startDate = getYearStartDate(year);
            endDate = getYearEndDate(year);
        }

        return applyTodayRestriction(startDate, endDate);
    }

    private OffsetDateTime getHistoricalStartDate() {
        return OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    }

    private OffsetDateTime getHistoricalEndDate() {
        return OffsetDateTime.of(2100, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC);
    }

    private OffsetDateTime getYearStartDate(Integer year) {
        return OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    }

    private OffsetDateTime getYearEndDate(Integer year) {
        return OffsetDateTime.of(year, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC);
    }

    private OffsetDateTime getMonthStartDate(Integer year, Integer month) {
        return YearMonth.of(year, month).atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    private OffsetDateTime getMonthEndDate(Integer year, Integer month) {
        return YearMonth.of(year, month).atEndOfMonth().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
    }

    private OffsetDateTime[] applyTodayRestriction(OffsetDateTime start, OffsetDateTime end) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime finalEndDate = end.isAfter(now) ? now : end;
        
        return new OffsetDateTime[] { start, finalEndDate };
    }

}