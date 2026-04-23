package com.verisure.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.Sdg;

@Repository
public interface SdgRepository extends JpaRepository<Sdg, Integer> {
    
}