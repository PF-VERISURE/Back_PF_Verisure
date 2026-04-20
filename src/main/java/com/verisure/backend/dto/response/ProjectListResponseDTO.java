package com.verisure.backend.dto.response;

import java.util.List;

public record ProjectListResponseDTO(

        List<ProjectResponseDTO> data,
        int totalElements) {

}