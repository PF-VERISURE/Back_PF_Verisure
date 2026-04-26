package com.verisure.backend.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.verisure.backend.dto.response.CategoryCountResponseDTO;
import com.verisure.backend.dto.response.DashboardKpiResponseDTO;
import com.verisure.backend.service.DashboardService;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/projectsbycategory")
    public ResponseEntity<List<CategoryCountResponseDTO>> getProjectsByCategory(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        List<CategoryCountResponseDTO> response = dashboardService.getProjectsByCategory(year, month);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/applicationsbycategory")
    public ResponseEntity<List<CategoryCountResponseDTO>> getApplicationsByCategory(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        List<CategoryCountResponseDTO> response = dashboardService.getApplicationsByCategory(year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/kpis")
    public ResponseEntity<DashboardKpiResponseDTO> getDashboardKpis(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        DashboardKpiResponseDTO kpiData = dashboardService.getKpiDashboard(year, month);
        return ResponseEntity.ok(kpiData);
    }
}