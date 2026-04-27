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
import com.verisure.backend.dto.response.GnoContributionResponseDTO;
import com.verisure.backend.dto.response.MonthlyEvolutionResponseDTO;
import com.verisure.backend.dto.response.ParticipationFunnelResponseDTO;
import com.verisure.backend.dto.response.YearlyComparisonResponseDTO;
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

    // @GetMapping("/applicationsbycategory")
    // public ResponseEntity<List<CategoryCountResponseDTO>>
    // getApplicationsByCategory(
    // @RequestParam(required = false) Integer year,
    // @RequestParam(required = false) Integer month) {

    // List<CategoryCountResponseDTO> response =
    // dashboardService.getApplicationsByCategory(year, month);
    // return ResponseEntity.ok(response);
    // }

    @GetMapping("/participationfunnel")
    public ResponseEntity<ParticipationFunnelResponseDTO> getParticipationFunnel(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        ParticipationFunnelResponseDTO response = dashboardService.getParticipationFunnel(year, month);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/kpis")
    public ResponseEntity<DashboardKpiResponseDTO> getKpisDashboard(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        DashboardKpiResponseDTO response = dashboardService.getKpiDashboard(year, month);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/monthlyevolution")
    public ResponseEntity<List<MonthlyEvolutionResponseDTO>> getMonthlyEvolution(
            @RequestParam(required = false) Integer year) {
        List<MonthlyEvolutionResponseDTO> response = dashboardService.getMonthlyEvolution(year);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/yearlycomparison")
    public ResponseEntity<List<YearlyComparisonResponseDTO>> getYearlyComparison(
            @RequestParam(required = false) Integer year) {
        List<YearlyComparisonResponseDTO> response = dashboardService.getYearlyComparison(year);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/gnocontributions")
    public ResponseEntity<List<GnoContributionResponseDTO>> getGnoContributions(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        List<GnoContributionResponseDTO> response = dashboardService.getGnoContributions(year, month);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}