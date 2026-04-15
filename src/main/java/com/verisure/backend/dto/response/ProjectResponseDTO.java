package com.verisure.backend.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ProjectResponseDTO(
    Long id,
    String title,
    String description,
    String imageUrl,
    String status,
    Integer requiredVolunteers,
    String locationType,
    String address,
    String city,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    String impactUnit,
    String certificateTemplate,
    Long gnoId,
    String gnoName,
    List<String> sdgs,
    Long totalApplications,
    Long totalVolunteers,
    OffsetDateTime createdAt
){}
