package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.response.CategoryCountResponseDTO;
import com.verisure.backend.dto.response.DashboardKpiResponseDTO;
import com.verisure.backend.dto.response.MonthlyEvolutionResponseDTO;
import com.verisure.backend.dto.response.ParticipationFunnelResponseDTO;

public interface DashboardService {

    List<CategoryCountResponseDTO> getProjectsByCategory(Integer year, Integer month);

    // List<CategoryCountResponseDTO> getApplicationsByCategory(Integer year, Integer month);

    DashboardKpiResponseDTO getKpiDashboard(Integer year, Integer month);

    ParticipationFunnelResponseDTO getParticipationFunnel(Integer year, Integer month);

    List<MonthlyEvolutionResponseDTO> getMonthlyEvolution(Integer year);
}