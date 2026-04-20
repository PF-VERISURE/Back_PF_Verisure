package com.verisure.backend.dto.response;

import java.util.List;

public record AdminApplicationListResponseDTO(

    List<AdminApplicationResponseDTO> data,
    int totalElements

) {

}