package com.verisure.backend.dto.response;

public record UserAuthResponseDTO(

    String role,
    Object profileData
    
) {

}