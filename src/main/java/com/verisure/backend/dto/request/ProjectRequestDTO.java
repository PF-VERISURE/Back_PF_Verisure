package com.verisure.backend.dto.request;

import java.time.OffsetDateTime;
import java.util.List;

import com.verisure.backend.entity.enums.LocationType;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record ProjectRequestDTO(

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título es demasiado largo, máximo 150 caracteres")
    String title,

    @NotBlank(message = "La descripción es obligatoria")
    String description,

    @URL(message = "La URL de la imagen no es válida")
    String imageUrl,

    @NotNull(message = "Número de plazas es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 plaza")
    Integer requiredVolunteers,

    @NotNull(message = "El tipo de localización es obligatorio")
    LocationType locationType,

    String address,

    String city,

    @NotBlank(message = "La unidad de impacto es obligatoria")
    String impactUnit,

    @NotNull(message = "Fecha de inicio obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    OffsetDateTime startDate,

    @NotNull(message = "Fecha de fin obligatoria")
    @Future(message= "La fecha fin debe ser en el Futuro")
    OffsetDateTime endDate,

    @NotBlank(message = "La cantidad de horas es obligatoria")
    @Positive
    Integer totalHours,

    @NotNull
    Long gnoId,

    @NotEmpty(message = "Debe seleccionar una opción")
    List<Integer> sdgIds
){}
