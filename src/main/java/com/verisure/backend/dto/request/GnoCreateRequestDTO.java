package com.verisure.backend.dto.request;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record GnoCreateRequestDTO(

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", 
             message = "La contraseña debe contener mayúscula, minúscula, número y carácter especial")
    String password,

    @NotBlank(message = "El CIF es obligatorio")
    @Pattern(regexp = "^[a-zA-Z][0-9]{7}[0-9a-zA-Z]$", message = "El formato del CIF no es válido")
    String cif,

    @NotBlank(message = "El nombre de la organización es obligatorio")
    @Size(max = 100, message = "El nombre es demasiado largo")
    String organizationName,

    @NotBlank(message = "El nombre de contacto es obligatorio")
    String contactName,

    @NotBlank(message = "El teléfono es obligatorio")
    String contactPhone,

    @NotBlank(message = "El email de contacto es obligatorio")
    @Email(message = "El formato del email de contacto no es válido")
    String contactEmail,

    @URL(message = "Debe ser una URL válida")
    String website,

    @NotBlank(message = "La dirección es obligatoria")
    String address
) {

}