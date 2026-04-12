package com.verisure.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El formato del email no es válido")
  @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
  String email,

  @NotBlank(message = "La contraseña es obligatoria")
  String password

) {

}