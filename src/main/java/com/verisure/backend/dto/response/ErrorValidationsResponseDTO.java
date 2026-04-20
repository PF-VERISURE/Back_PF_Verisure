package com.verisure.backend.dto.response;

import java.time.OffsetDateTime;
import java.util.Map;

public record ErrorValidationsResponseDTO(

        OffsetDateTime timestamp,
        int status,
        String error,
        Map<String, String> fieldErrors

) {

    public ErrorValidationsResponseDTO(int status, String error, Map<String, String> fieldErrors) {
        this(OffsetDateTime.now(), status, error, fieldErrors);
    }

}
