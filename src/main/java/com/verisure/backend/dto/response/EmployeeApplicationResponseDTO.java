package com.verisure.backend.dto.response;

import java.time.OffsetDateTime;
import java.util.List;
import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.StatusApplication;

public record EmployeeApplicationResponseDTO(
    
    Long applicationId,
    StatusApplication status,
    OffsetDateTime appliedAt,
    Long projectId,
    String projectTitle,
    OffsetDateTime startDate,
    OffsetDateTime endDate,
    LocationType locationType,
    String imageUrl,
    Integer totalHours,
    List<String> sdgs

) {
    
}