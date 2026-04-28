package com.verisure.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.GnoProfile;

@Repository
public interface GnoProfileRepository extends JpaRepository<GnoProfile, Long> {
    
    @Query("SELECT g FROM GnoProfile g WHERE g.user.id = :userId AND g.user.deletedAt IS NULL")
    Optional<GnoProfile> findByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(g) > 0 FROM GnoProfile g WHERE g.cif = :cif AND g.user.deletedAt IS NULL")
    boolean existsByCif(@Param("cif") String cif);

}