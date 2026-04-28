package com.verisure.backend.entity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
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

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "projects_sdgs", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "sdg_id"))

  private List<Sdg> sdgs = new ArrayList<>();

  @Column(nullable = false, length = 150)
  private String title;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String description;

  @Column(name = "image_url", length = 1000)
  private String imageUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusProject status = StatusProject.PENDING;

  @Column(nullable = false)
  private Integer requiredVolunteers;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LocationType locationType;

  @Column(nullable = false, length = 500)
  private String address;

  @Column(nullable = true, length = 100)
  private String city;

  @Column(nullable = false, name = "impact_unit")
  private String impactUnit;

  @Column(nullable = false)
  private Integer totalHours;

  @Column(name = "certificate_template")
  private String certificateTemplate;

  @Column(nullable = false)
  private OffsetDateTime startDate;

  @Column(nullable = false)
  private OffsetDateTime endDate;

  @Column(nullable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(nullable = true)
  private OffsetDateTime updatedAt;

  
  @PrePersist
  protected void onCreate() {
    if (this.createdAt == null) {
      this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
  }

}