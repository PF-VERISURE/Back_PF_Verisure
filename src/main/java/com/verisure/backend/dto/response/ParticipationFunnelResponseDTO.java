package com.verisure.backend.dto.response;

public record ParticipationFunnelResponseDTO(

    Long enrolled,
    Long waitlist,
    Long likes

) {

}