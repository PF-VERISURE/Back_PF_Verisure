package com.verisure.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeCreateRequestDTO(

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email no válido")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    String password,

    @NotNull(message = "El número de empleado es obligatorio")
    @Min(value = 1, message = "El número de empleado no es válido")
    Long employeeId,

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre es demasiado largo")
    String firstName,

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido es demasiado largo")
    String lastName,

    @NotBlank(message = "El departamento es obligatorio")
    String department
    
) {

}