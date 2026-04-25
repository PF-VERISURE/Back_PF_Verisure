package com.verisure.backend.service;

import java.util.List;

import com.verisure.backend.dto.response.CategoryCountResponseDTO;

public interface DashboardService {

    List<CategoryCountResponseDTO> getProjectsByCategory(Integer year, Integer month);

    List<CategoryCountResponseDTO> getApplicationsByCategory(Integer year, Integer month);

}