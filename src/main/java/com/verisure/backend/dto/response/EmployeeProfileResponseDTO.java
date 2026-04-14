package com.verisure.backend.dto.response;

public record EmployeeProfileResponseDTO(

    Long id,
    String email,
    Long employeeId,
    String firstName,
    String lastName,
    String department

) {

}