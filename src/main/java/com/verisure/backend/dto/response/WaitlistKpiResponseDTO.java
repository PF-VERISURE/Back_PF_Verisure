package com.verisure.backend.dto.response;

public record WaitlistKpiResponseDTO(

    Long totalWaitlist,
    String unfilledDemand
    
) {

}