package com.verisure.backend.dto.response;

public record MonthlyEvolutionResponseDTO(

    String month,
    Long hours,
    Long volunteers 

) {

}