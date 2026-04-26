package com.verisure.backend.dto.response;

public record CategoryComparisonResponseDTO(

        String categoryName,
        Long likesCount,
        Long applicationsCount

) {

}