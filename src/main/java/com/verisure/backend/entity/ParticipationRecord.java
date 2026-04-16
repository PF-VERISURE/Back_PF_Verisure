package com.verisure.backend.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "participation_records")
@Data
@NoArgsConstructor

public class ParticipationRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "application_id", unique = true, nullable = false, referencedColumnName = "id")
  private Application application;

  // @NotNull(message = "Las horas registradas son obligatorias")
  // @DecimalMin(value = "0.0", inclusive = false, message = "Las horas deben ser mayores a cero")
  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal loggedHours;

  // @NotNull(message = "La métrica de impacto es obligatoria")
  // @DecimalMin(value = "0.0", message = "El impacto no puede ser negativo")
  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal impactMetric;

  @Column(name = "certificate_url")
  private String certificateUrl;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private OffsetDateTime createdAt;

}
  
