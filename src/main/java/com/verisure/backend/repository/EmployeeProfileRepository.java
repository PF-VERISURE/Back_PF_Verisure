package com.verisure.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.EmployeeProfile;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
  
    @Query("SELECT e FROM EmployeeProfile e WHERE e.user.id = :userId AND e.user.deletedAt IS NULL")
    Optional<EmployeeProfile> findByUserId(@Param("userId") Long userId);
    
}