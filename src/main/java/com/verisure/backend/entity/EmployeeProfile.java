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
@Table(name = "employee_profiles")
@Data
@NoArgsConstructor

public class EmployeeProfile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true, nullable = false, referencedColumnName = "id")
  private User user;

  //valiadaciones de formato para dto comentadas.
  // @NotNull(message = "El número de empleado es obligatorio")
  // @Min(value = 1, message = "El número de empleado no es válido")
  @Column(unique = true, nullable = false)
  private Long employeeId;

  // @NotBlank(message = "El nombre no puede estar vacío")
  // @Size(max = 50, message = "El nombre es demasiado largo")
  @Column(nullable = false, length = 50)
  private String firstName;

  // @NotBlank(message = "El apellido no puede estar vacío")
  // @Size(max = 100, message = "El apellido es demasiado largo")
  @Column(nullable = false, length = 100)
  private String lastName;

  // @NotBlank(message = "El departamento es obligatorio")
  @Column(nullable = false)
  private String department;

}