package com.verisure.backend.dto.response;

import java.util.List;

public record EmployeeProfileListResponseDTO(

    List<EmployeeProfileResponseDTO> data,
    int totalElements
        
) {

}