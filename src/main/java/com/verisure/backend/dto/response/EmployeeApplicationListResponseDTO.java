package com.verisure.backend.dto.response;

import java.util.List;

public record EmployeeApplicationListResponseDTO(

    List<EmployeeApplicationResponseDTO> data,
    int totalElements

) {

}