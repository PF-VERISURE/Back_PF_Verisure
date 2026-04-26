package com.verisure.backend.entity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import com.verisure.backend.entity.enums.StatusApplication;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.CascadeType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "project_id", "employee_id" }) })
@Data
@NoArgsConstructor
public class Application {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id", nullable = false, referencedColumnName = "id")
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false, referencedColumnName = "id")
  private EmployeeProfile employee;

  @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private ParticipationRecord participationRecord;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StatusApplication status;

  // @CreationTimestamp
  @Column(nullable = false)
  private OffsetDateTime createdAt;

  // @UpdateTimestamp
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