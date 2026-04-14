package com.verisure.backend.dto.request;

import java.time.OffsetDateTime;
import java.util.List;

import com.verisure.backend.entity.enums.LocationType;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

import lombok.Data;

@Data
public class ProjectRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título es demasiado largo, máximo 150 caracteres")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @URL(message = "La URL de la imagen no es válida")
    private String imageUrl;

    @NotNull(message = "Número de plazas es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 plaza")
    private Integer requiredVolunteers;

    @NotNull(message = "El tipo de localización es obligatorio")
    private LocationType locationType;

    private String address;

    private String city;

    @NotBlank(message = "La unidad de impacto es obligatoria")
    private String impactUnit;

    private String certificateTemplate;

    @NotNull(message = "Fecha de inicio obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private OffsetDateTime startDate;

    @NotNull(message = "Fecha de fin obligatoria")
    @Future(message= "La fecha fin debe ser en el Futuro")
    private OffsetDateTime endDate;

    @NotEmpty(message = "Debe seleccionar al menos un opción")
    private List<Long> sdgIds;
}
