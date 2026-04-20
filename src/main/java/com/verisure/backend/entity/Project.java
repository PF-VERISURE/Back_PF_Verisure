package com.verisure.backend.entity;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.URL;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.StatusProject;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gno_id", nullable = false, referencedColumnName = "id")
  private GnoProfile gno;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "admin_id", referencedColumnName = "id")
  private User admin;
  
  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Application> applications = new ArrayList<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserFavorite> favorites = new ArrayList<>();

  // las tablas intermedias N:M
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "projects_sdgs", 
      joinColumns = @JoinColumn(name = "project_id"),
      inverseJoinColumns = @JoinColumn(name = "sdg_id")
  )

  private List<Sdg> sdgs = new ArrayList<>();

  //valiadaciones de formato para dto comentadas.
  // @NotBlank(message = "El título es obligatorio")
  // @Size(max = 150, message = "El título es demasiado largo")
  @Column(nullable = false, length = 150)
  private String title;
  
  // @NotBlank(message = "La descripción es obligatoria")
  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;

  @URL(message = "La URL de la imagen no es válida")
  @Column(name = "image_url")
  private String imageUrl;

  // @NotNull(message = "El estado del proyecto es obligatorio")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusProject status = StatusProject.PENDING;

  // @NotNull(message = "El número de voluntarios es obligatorio")
  // @Min(value = 1, message = "Debe haber al menos 1 plaza")
  @Column(nullable = false)
  private Integer requiredVolunteers;
  
  // @NotNull(message = "El tipo de localización es obligatorio")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LocationType locationType; // Enum { ONLINE, IN_PERSON, HYBRID }

  @Column(nullable = true)
  private String address; // NO NECESARIO MVP.
  
  @Column(nullable = true)
  private String city; // NO NECESARIO MVP.

  // @NotBlank(message = "La unidad de impacto es obligatoria")
  @Column(name = "impact_unit")
  private String impactUnit;

  @Column(nullable = false)
  private Integer totalHours;

  @Column(name = "certificate_template")
  private String certificateTemplate;

  // @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
  @Column(nullable = false)
  private OffsetDateTime startDate;

  // @Future(message = "La fecha de fin debe ser en el futuro")
  @Column(nullable = false)
  private OffsetDateTime endDate;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @UpdateTimestamp 
  @Column(nullable = true)
  private OffsetDateTime updatedAt;  
}