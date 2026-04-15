package com.verisure.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ApplicationRequestDTO(

    @NotNull(message = "El identificador del proyecto es obligatorio")
    @Positive(message = "El ID del proyecto debe ser válido")
    Long projectId
    
) {

}