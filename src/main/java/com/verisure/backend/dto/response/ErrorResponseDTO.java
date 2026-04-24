package com.verisure.backend.dto.response;

import java.time.OffsetDateTime;

public record ErrorResponseDTO(

    OffsetDateTime timestamp,
    int status,
    String error

) {

    public ErrorResponseDTO(int status, String error) {
        this(OffsetDateTime.now(), status, error);
    }

}