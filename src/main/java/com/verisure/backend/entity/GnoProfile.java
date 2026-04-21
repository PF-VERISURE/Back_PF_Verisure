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

  @Column(unique = true, nullable = false, length = 9)
  private String cif;

  @Column(nullable = false, length = 100)
  private String organizationName;

  @Column(nullable = false)
  private String contactName;

  @Column(nullable = false, length = 20)
  private String contactPhone;

  @Column(nullable = false, length = 100)
  private String contactEmail;

  @Column(nullable = true)
  private String website;

  @Column(nullable = false)
  private String address;
  
}