package com.verisure.backend.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sdgs")
@Data
@NoArgsConstructor
public class Sdg {

  @Id
  private Integer id;
  
  @Column(nullable = false, unique = true)
  private String name;

    @ManyToMany(mappedBy = "sdgs", fetch = FetchType.LAZY)
  private List<Project> projects = new ArrayList<>();
  
}
