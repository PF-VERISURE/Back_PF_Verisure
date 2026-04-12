package com.verisure.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gno_profiles")
@Data
@NoArgsConstructor
public class GnoProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true, nullable = false, referencedColumnName = "id")
  private User user;

  //valiadaciones de formato para dto comentadas.
  // @NotBlank(message = "El CIF es obligatorio")
  // @Pattern(regexp = "^[a-zA-Z][0-9]{7}[0-9a-zA-Z]$", message = "El formato del CIF no es válido")
  @Column(unique = true, nullable = false)
  private String cif;

  // @NotBlank(message = "El nombre de la organización es obligatorio")
  // @Size(max = 100, message = "El nombre es demasiado largo")
  @Column(nullable = false)
  private String organizationName;

  // @NotBlank(message = "El nombre de contacto es obligatorio")
  @Column(nullable = false)
  private String contactName;

  // @NotBlank(message = "El teléfono es obligatorio")
  @Column(nullable = false)
  private String contactPhone;

  // @NotBlank(message = "El email de contacto es obligatorio")
  // @Email(message = "El formato del email no es válido")
  @Column(nullable = false)
  private String contactEmail;

  // @URL(message = "Debe ser una URL válida")
  @Column(nullable = true)
  private String website;

  // @NotBlank(message = "La dirección es obligatoria")
  @Column(nullable = false)
  private String address;
  
}