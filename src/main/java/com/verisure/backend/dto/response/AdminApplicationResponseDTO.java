package com.verisure.backend.dto.response;

import java.time.OffsetDateTime;

import com.verisure.backend.entity.enums.StatusApplication;

public record AdminApplicationResponseDTO(

    Long applicationId,
    StatusApplication status,
    OffsetDateTime appliedAt,
    Long projectId,
    String projectTitle,
    Long corporateEmployeeId,
    String firstName,
    String lastName,
    String department

) {    

}