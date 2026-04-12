package com.verisure.backend.dto.response;

import com.verisure.backend.entity.enums.Role;

public record UserAuthResponseDTO(

    String email,
    Role role
) {

}
