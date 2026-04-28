package com.verisure.backend.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.verisure.backend.dto.response.CategoryCountResponseDTO;
import com.verisure.backend.dto.response.ConversionKpiResponseDTO;
import com.verisure.backend.dto.response.DashboardKpiResponseDTO;
import com.verisure.backend.dto.response.GnoContributionResponseDTO;
import com.verisure.backend.dto.response.HoursKpiResponseDTO;
import com.verisure.backend.dto.response.MonthlyEvolutionResponseDTO;
import com.verisure.backend.dto.response.ParticipationFunnelResponseDTO;
import com.verisure.backend.dto.response.ProjectsKpiResponseDTO;
import com.verisure.backend.dto.response.TopCategoryKpiResponseDTO;
import com.verisure.backend.dto.response.VolunteersKpiResponseDTO;
import com.verisure.backend.dto.response.WaitlistKpiResponseDTO;
import com.verisure.backend.dto.response.YearlyComparisonResponseDTO;
import com.verisure.backend.entity.enums.StatusApplication;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.EmployeeProfileRepository;
import com.verisure.backend.repository.ParticipationRecordRepository;
import com.verisure.backend.repository.ProjectRepository;
import com.verisure.backend.repository.UserFavoriteRepository;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImp implements DashboardService {

    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
    private final ParticipationRecordRepository participationRecordRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final UserFavoriteRepository userFavoriteRepository;

    public DashboardServiceImp(ProjectRepository projectRepository, ApplicationRepository applicationRepository,
            ParticipationRecordRepository participationRecordRepository,
            EmployeeProfileRepository employeeProfileRepository,
            UserFavoriteRepository userFavoriteRepository) {
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.participationRecordRepository = participationRecordRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.userFavoriteRepository = userFavoriteRepository;
    }

    @Override
    public List<CategoryCountResponseDTO> getProjectsByCategory(Integer year, Integer month) {

        OffsetDateTime[] dateRange = calculateDateRange(year, month);
        List<Object[]> rawData = projectRepository.countProjectsByCategoryRaw(dateRange[0], dateRange[1]);
        return mapToCategoryCountDTOs(rawData);
    }

    @Override
    public ParticipationFunnelResponseDTO getParticipationFunnel(Integer year, Integer month) {
        OffsetDateTime[] range = calculateDateRange(year, month);

        long enrolled = applicationRepository.countApplicationsByStatusesAndDateRange(
                List.of(StatusApplication.APPROVED, StatusApplication.CLOSED),
                range[0], range[1]);

        long waitlist = applicationRepository.countApplicationsByStatusesAndDateRange(
                List.of(StatusApplication.WAITLISTED),
                range[0], range[1]);

        long likes = userFavoriteRepository.countFavoritesByDateRange(range[0], range[1]);

        return new ParticipationFunnelResponseDTO(enrolled, waitlist, likes);
    }

    @Override
    public DashboardKpiResponseDTO getKpiDashboard(Integer year, Integer month) {
        OffsetDateTime[] currentRange = calculateDateRange(year, month);
        OffsetDateTime startDate = currentRange[0];
        OffsetDateTime endDate = currentRange[1];

        List<StatusApplication> activeAppStatuses = List.of(StatusApplication.APPROVED, StatusApplication.CLOSED);
        List<StatusProject> activeProjStatuses = List.of(StatusProject.PUBLISHED);

        return new DashboardKpiResponseDTO(
                buildHoursKpi(startDate, endDate),
                buildVolunteersKpi(activeAppStatuses, startDate, endDate),
                buildTopCategoryKpi(activeAppStatuses, startDate, endDate),
                buildProjectsKpi(activeProjStatuses, startDate, endDate),
                buildConversionKpi(activeAppStatuses, startDate, endDate),
                buildWaitlistKpi(activeProjStatuses, startDate, endDate));
    }

    @Override
    public List<MonthlyEvolutionResponseDTO> getMonthlyEvolution(Integer year) {
        int targetYear = (year != null) ? year : OffsetDateTime.now(ZoneOffset.UTC).getYear();

        OffsetDateTime startOfYear = getYearStartDate(targetYear);
        OffsetDateTime endOfYear = getYearEndDate(targetYear);

        List<StatusApplication> activeStatuses = List.of(StatusApplication.APPROVED, StatusApplication.CLOSED);
        List<Object[]> hoursRaw = participationRecordRepository.sumTotalHoursByMonthRaw(startOfYear, endOfYear);
        List<Object[]> volunteersRaw = applicationRepository.countDistinctVolunteersByMonthRaw(activeStatuses,
                startOfYear, endOfYear);

        long[] hoursByMonth = new long[13];
        long[] volunteersByMonth = new long[13];

        for (Object[] row : hoursRaw) {
            int month = ((Number) row[0]).intValue();
            long hours = ((Number) row[1]).longValue();
            hoursByMonth[month] = hours;
        }

        for (Object[] row : volunteersRaw) {
            int month = ((Number) row[0]).intValue();
            long volunteers = ((Number) row[1]).longValue();
            volunteersByMonth[month] = volunteers;
        }

        List<MonthlyEvolutionResponseDTO> evolution = new ArrayList<>();
        String[] monthNames = { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" };

        for (int i = 1; i <= 12; i++) {
            evolution.add(new MonthlyEvolutionResponseDTO(monthNames[i - 1],
                    hoursByMonth[i], volunteersByMonth[i]));
        }

        return evolution;
    }

    @Override
    public List<YearlyComparisonResponseDTO> getYearlyComparison(Integer year) {
        int targetYear = (year != null) ? year : OffsetDateTime.now(ZoneOffset.UTC).getYear();
        int startYear = targetYear - 4;

        OffsetDateTime startOfPeriod = getYearStartDate(startYear);
        OffsetDateTime endOfPeriod = getYearEndDate(targetYear);
        OffsetDateTime[] restrictedRange = applyTodayRestriction(startOfPeriod,
                endOfPeriod);
        OffsetDateTime finalStart = restrictedRange[0];
        OffsetDateTime finalEnd = restrictedRange[1];

        List<StatusApplication> activeStatuses = List.of(StatusApplication.APPROVED,
                StatusApplication.CLOSED);
        List<StatusProject> activeProjStatuses = List.of(StatusProject.PUBLISHED,
                StatusProject.COMPLETED);

        List<Object[]> hoursRaw = participationRecordRepository.sumTotalHoursByYearRaw(finalStart, finalEnd);
        List<Object[]> volunteersRaw = applicationRepository.countDistinctVolunteersByYearRaw(activeStatuses,
                finalStart, finalEnd);
        List<Object[]> projectsRaw = projectRepository.countProjectsByYearRaw(activeProjStatuses, finalStart,
                finalEnd);

        Map<Integer, Long> hoursByYear = new HashMap<>();
        Map<Integer, Long> volunteersByYear = new HashMap<>();
        Map<Integer, Long> projectsByYear = new HashMap<>();

        for (Object[] row : hoursRaw) {
            hoursByYear.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
        }
        for (Object[] row : volunteersRaw) {
            volunteersByYear.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
        }
        for (Object[] row : projectsRaw) {
            projectsByYear.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
        }

        List<YearlyComparisonResponseDTO> comparison = new ArrayList<>();

        for (int y = startYear; y <= targetYear; y++) {

            long hours = hoursByYear.getOrDefault(y, 0L);
            long volunteers = volunteersByYear.getOrDefault(y, 0L);
            long projects = projectsByYear.getOrDefault(y, 0L);

            comparison.add(new YearlyComparisonResponseDTO(y, hours, volunteers,
                    projects));
        }

        return comparison;
    }

    @Override
    public List<GnoContributionResponseDTO> getGnoContributions(Integer year, Integer month) {
        OffsetDateTime[] range = calculateDateRange(year, month);
        List<StatusApplication> activeStatuses = List.of(StatusApplication.APPROVED, StatusApplication.CLOSED);

        List<Object[]> rawData = applicationRepository.getGnoContributionsRaw(activeStatuses, range[0], range[1]);

        List<GnoContributionResponseDTO> result = new ArrayList<>();
        for (Object[] row : rawData) {
            String gnoName = (String) row[0];
            Long hours = ((Number) row[1]).longValue();
            Long volunteers = ((Number) row[2]).longValue();

            result.add(new GnoContributionResponseDTO(gnoName, hours, volunteers));
        }

        return result;
    }

    private HoursKpiResponseDTO buildHoursKpi(OffsetDateTime startDate, OffsetDateTime endDate) {
        OffsetDateTime lastYearStart = startDate.minusYears(1);
        OffsetDateTime lastYearEnd = endDate.minusYears(1);

        BigDecimal currentHoursBd = participationRecordRepository.sumTotalHoursByDateRange(startDate, endDate);
        BigDecimal lastYearHoursBd = participationRecordRepository.sumTotalHoursByDateRange(lastYearStart, lastYearEnd);

        long currentHours = currentHoursBd.longValue();
        long lastYearHours = lastYearHoursBd.longValue();
        String yoyTrend = calculateYoY(currentHours, lastYearHours);

        return new HoursKpiResponseDTO(currentHours, yoyTrend);
    }

    private VolunteersKpiResponseDTO buildVolunteersKpi(List<StatusApplication> statuses, OffsetDateTime startDate,
            OffsetDateTime endDate) {
        long activeVolunteers = applicationRepository.countDistinctVolunteersByStatusesAndDateRange(statuses, startDate,
                endDate);
        long totalEmployees = employeeProfileRepository.count();
        String workforcePct = calculatePercentage(activeVolunteers, totalEmployees) + "% de la plantilla elegible";

        return new VolunteersKpiResponseDTO(activeVolunteers, workforcePct);
    }

    private TopCategoryKpiResponseDTO buildTopCategoryKpi(List<StatusApplication> statuses, OffsetDateTime startDate,
            OffsetDateTime endDate) {
        String topCategoryName = applicationRepository
                .findTopSdgNameByStatusesAndDateRange(statuses, startDate, endDate)
                .orElse("Sin datos suficientes");

        return new TopCategoryKpiResponseDTO(topCategoryName);
    }

    private ProjectsKpiResponseDTO buildProjectsKpi(List<StatusProject> statuses, OffsetDateTime startDate,
            OffsetDateTime endDate) {
        long activeProjects = projectRepository.countProjectsByStatusesAndDateRange(statuses, startDate, endDate);
        long collaboratingGnos = projectRepository.countDistinctGnosByProjectStatusesAndDateRange(statuses, startDate,
                endDate);
        String gnosText = collaboratingGnos + " GNOs colaboradoras";

        return new ProjectsKpiResponseDTO(activeProjects, gnosText);
    }

    private ConversionKpiResponseDTO buildConversionKpi(List<StatusApplication> statuses, OffsetDateTime startDate,
            OffsetDateTime endDate) {
        long likes = userFavoriteRepository.countFavoritesByDateRange(startDate, endDate);
        long enrolled = applicationRepository.countApplicationsByStatusesAndDateRange(statuses, startDate, endDate);
        String conversionRate = "Tasa de conversión " + calculatePercentage(enrolled, likes) + "%";

        return new ConversionKpiResponseDTO(likes, enrolled, conversionRate);
    }

    private WaitlistKpiResponseDTO buildWaitlistKpi(List<StatusProject> projStatuses, OffsetDateTime startDate,
            OffsetDateTime endDate) {
        long waitlisted = applicationRepository
                .countApplicationsByStatusesAndDateRange(List.of(StatusApplication.WAITLISTED), startDate, endDate);
        long requiredPlazas = projectRepository.sumRequiredVolunteersByStatusesAndDateRange(projStatuses, startDate,
                endDate);
        String demandText = "Demanda no cubierta — +" + calculatePercentage(waitlisted, requiredPlazas)
                + "% sobre plazas";

        return new WaitlistKpiResponseDTO(waitlisted, demandText);
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

    private String calculateYoY(long current, long previous) {
        if (previous == 0) {
            return current > 0 ? "+100% YoY" : "0% YoY";
        }
        long diff = current - previous;
        long percentage = (diff * 100) / previous;

        String sign = percentage > 0 ? "+" : "";
        return sign + percentage + "% YoY";
    }

    private long calculatePercentage(long part, long total) {
        if (total == 0) {
            return 0;
        }
        return (part * 100) / total;
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