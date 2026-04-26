package com.verisure.backend.dto.response;

public record DashboardKpiResponseDTO(

    HoursKpiResponseDTO hours,
    VolunteersKpiResponseDTO volunteers,
    TopCategoryKpiResponseDTO topCategory,
    ProjectsKpiResponseDTO projects,
    ConversionKpiResponseDTO conversion,
    WaitlistKpiResponseDTO waitlist

) {

}