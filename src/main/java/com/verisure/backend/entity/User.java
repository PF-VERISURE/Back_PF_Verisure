package com.verisure.backend.entity;

import java.time.OffsetDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.verisure.backend.entity.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor

public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //valiadaciones de formato para dto comentadas.
  //@NotBlank(message = "El email es obligatorio") 
  //@Email(message = "El formato del email no es válido")
  //@Size(max = 100, message = "El email no puede exceder los 100 caracteres")
  @Column(unique = true, nullable = false)
  private String email;

  // @NotBlank(message = "La contraseña es obligatoria")
  // @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
  // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$", 
  // message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial")
  @Column(nullable = false)
  private String passwordHash;

  //@NotNull(message = "El rol es obligatorio")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "deleted_at")
  private OffsetDateTime deletedAt;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private EmployeeProfile employeeProfile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private GnoProfile gnoProfile;

}