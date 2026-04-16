package com.verisure.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.verisure.backend.entity.GnoProfile;

@Repository
public interface GnoProfileRepository extends JpaRepository<GnoProfile, Long> {

    Optional<GnoProfile> findByCif(String cif);
    
    Optional<GnoProfile> findByUserId(Long userId);
    
    boolean existsByCif(String cif);

}