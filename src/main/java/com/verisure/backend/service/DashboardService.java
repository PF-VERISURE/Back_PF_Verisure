package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.response.CategoryCountResponseDTO;
import com.verisure.backend.dto.response.DashboardKpiResponseDTO;

public interface DashboardService {

    List<CategoryCountResponseDTO> getProjectsByCategory(Integer year, Integer month);

    List<CategoryCountResponseDTO> getApplicationsByCategory(Integer year, Integer month);

    DashboardKpiResponseDTO getKpiDashboard(Integer year, Integer month);
}