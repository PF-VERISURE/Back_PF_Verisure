package com.verisure.backend.dto.response;

public record GnoProfileResponseDTO(

    String email,
    String cif,
    String organizationName,
    String contactName,
    String contactPhone,
    String contactEmail,
    String website,
    String address

) {

}