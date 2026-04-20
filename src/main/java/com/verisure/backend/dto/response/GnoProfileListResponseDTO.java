package com.verisure.backend.dto.response;

import java.util.List;

public record GnoProfileListResponseDTO(

    List<GnoProfileResponseDTO> gnoProfiles,
    int total

) {

}