package com.verisure.backend.repository;

import com.verisure.backend.entity.Sdg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SdgRepository extends JpaRepository<Sdg, Integer>{
    
}
