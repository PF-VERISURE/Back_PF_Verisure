package com.verisure.backend.repository;

import java.time.OffsetDateTime;
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

    List<Application> findByProjectId(Long projectId);

    Optional<Application> findByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    List<Application> findAllByOrderByCreatedAtDesc();

    List<Application> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    Long countByProjectIdAndStatus(Long projectId, StatusApplication status);


    @Query("""
        SELECT a 
        FROM Application a 
        WHERE a.project.id = :projectId 
        AND a.status = :status
        ORDER BY a.createdAt ASC 
        LIMIT 1
    """)
    Optional<Application> findNextInWaitlist(@Param("projectId") Long projectId, @Param("status") StatusApplication status);

    @Query("""
        SELECT COUNT(DISTINCT a.employee.id) 
        FROM Application a 
        WHERE a.status IN :statuses 
        AND a.createdAt BETWEEN :startDate AND :endDate
    """)
    Long countDistinctVolunteersByStatusesAndDateRange(
        @Param("statuses") List<StatusApplication> statuses, 
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    @Query("""
        SELECT COUNT(a) 
        FROM Application a 
        WHERE a.status IN :statuses 
        AND a.createdAt BETWEEN :startDate AND :endDate
    """)
    Long countApplicationsByStatusesAndDateRange(
        @Param("statuses") List<StatusApplication> statuses, 
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    @Query("""
        SELECT MONTH(a.project.endDate), COUNT(DISTINCT a.employee)
        FROM Application a
        WHERE a.status IN :statuses
        AND a.project.endDate BETWEEN :startDate AND :endDate
        GROUP BY MONTH(a.project.endDate)
    """)
    List<Object[]> countDistinctVolunteersByMonthRaw(
        @Param("statuses") List<StatusApplication> statuses, 
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    @Query("""
        SELECT YEAR(a.project.endDate), COUNT(DISTINCT a.employee)
        FROM Application a
        WHERE a.status IN :statuses
        AND a.project.endDate BETWEEN :startDate AND :endDate
        GROUP BY YEAR(a.project.endDate)
    """)
    List<Object[]> countDistinctVolunteersByYearRaw(
        @Param("statuses") List<StatusApplication> statuses, 
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    @Query("""
        SELECT s.name 
        FROM Application a 
        JOIN a.project p 
        JOIN p.sdgs s 
        WHERE a.status IN :statuses 
        AND a.createdAt BETWEEN :startDate AND :endDate 
        GROUP BY s.name 
        ORDER BY COUNT(a.id) DESC
        LIMIT 1
    """)
    Optional<String> findTopSdgNameByStatusesAndDateRange(
        @Param("statuses") List<StatusApplication> statuses, 
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    @Query("""
        SELECT p.gno.organizationName, 
               COALESCE(SUM(pr.loggedHours), 0), 
               COUNT(DISTINCT a.employee)
        FROM Application a
        LEFT JOIN a.participationRecord pr
        JOIN a.project p
        WHERE a.status IN :statuses
        AND p.endDate BETWEEN :startDate AND :endDate
        GROUP BY p.gno.organizationName
        ORDER BY COALESCE(SUM(pr.loggedHours), 0) DESC
    """)
    List<Object[]> getGnoContributionsRaw(
            @Param("statuses") List<StatusApplication> statuses,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate
    );

}