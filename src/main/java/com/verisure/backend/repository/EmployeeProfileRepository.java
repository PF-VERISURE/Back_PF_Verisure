package com.verisure.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.EmployeeProfile;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    
    Optional<EmployeeProfile> findByEmployeeId(Long employeeId);

    Optional<EmployeeProfile> findByUserId(Long userId);

    boolean existsByEmployeeId(Long employeeId);

    //List<EmployeeProfile> findByDepartment(String department); Queremos estadisticas por departamento? NO MVP

}