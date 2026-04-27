package com.verisure.backend.dto.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record CertificateResponseDTO(

    Long participationId,
    String volunteerFullName,
    String projectTitle,
    List<String> sdgs,
    String gnoName,
    BigDecimal loggedHours,
    OffsetDateTime endDate

) {

}