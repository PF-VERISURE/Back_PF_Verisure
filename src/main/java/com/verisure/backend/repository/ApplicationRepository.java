package com.verisure.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.enums.StatusApplication;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    Optional<Application> findByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    List<Application> findByProjectIdAndStatus(Long projectId, StatusApplication status);

    List<Application> findAllByOrderByCreatedAtDesc();

    @Query("SELECT a FROM Application a WHERE a.employee.id = :employeeId ORDER BY a.createdAt DESC")
    List<Application> findEmployeeHistory(@Param("employeeId") Long employeeId);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.project.id = :projectId AND a.status = :status")
    long countProjectOccupancy(@Param("projectId") Long projectId, @Param("status") StatusApplication status);

    @Query("SELECT a FROM Application a WHERE a.project.id = :projectId AND a.status = :status ORDER BY a.createdAt ASC LIMIT 1")
    Optional<Application> findNextInWaitlist(@Param("projectId") Long projectId, @Param("status") StatusApplication status);

}