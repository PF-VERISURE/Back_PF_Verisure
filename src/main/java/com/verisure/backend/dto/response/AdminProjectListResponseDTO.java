package com.verisure.backend.dto.response;

import java.util.List;

public record AdminProjectListResponseDTO(
    List<AdminProjectResponseDTO> data,
    int totalElements
) {}
