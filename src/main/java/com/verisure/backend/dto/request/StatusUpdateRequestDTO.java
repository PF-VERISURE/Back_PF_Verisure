package com.verisure.backend.dto.request;

import com.verisure.backend.entity.enums.StatusProject;

import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequestDTO(

    @NotNull(message = "El estado no puede ser nulo")
    StatusProject status
) {}
