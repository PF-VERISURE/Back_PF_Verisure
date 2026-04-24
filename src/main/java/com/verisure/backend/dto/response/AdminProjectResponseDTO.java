package com.verisure.backend.dto.response;

import java.time.OffsetDateTime;

public record AdminProjectResponseDTO(

    Long id,
    String title,
    String status,
    String gnoName,
    Integer requiredVolunteers,
    Long totalApplications,
    Long totalFavorites,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    OffsetDateTime createdAt
) {
    
}
