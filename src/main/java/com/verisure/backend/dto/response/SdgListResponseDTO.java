package com.verisure.backend.dto.response;

import java.util.List;

public record SdgListResponseDTO(

    List<SdgResponseDTO> data,
    int totalElements

) {

}