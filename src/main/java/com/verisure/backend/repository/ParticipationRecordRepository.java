package com.verisure.backend.repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.ParticipationRecord;

@Repository
public interface ParticipationRecordRepository extends JpaRepository<ParticipationRecord, Long> {

    @Query("""
        SELECT COALESCE(SUM(p.loggedHours), 0) 
        FROM ParticipationRecord p 
        WHERE p.createdAt BETWEEN :startDate AND :endDate
    """)
    BigDecimal sumTotalHoursByDateRange(
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    @Query("""
        SELECT MONTH(p.application.project.endDate), COALESCE(SUM(p.loggedHours), 0)
        FROM ParticipationRecord p
        WHERE p.application.project.endDate BETWEEN :startDate AND :endDate
        GROUP BY MONTH(p.application.project.endDate)
    """)
    List<Object[]> sumTotalHoursByMonthRaw(
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    @Query("""
        SELECT pr.id,
               a.employee.firstName, 
               a.employee.lastName,
               p.title,
               p.gno.organizationName,
               pr.loggedHours,
               p.endDate,
               s.name
        FROM ParticipationRecord pr
        JOIN pr.application a
        JOIN a.project p
        LEFT JOIN p.sdgs s
        WHERE pr.id = :participationId AND a.employee.user.id = :userId
    """)
    List<Object[]> findRawCertificateById(@Param("participationId") Long participationId, @Param("userId") Long userId);

    @Query("""
        SELECT pr.id,
               a.employee.firstName, 
               a.employee.lastName,
               p.title,
               p.gno.organizationName,
               pr.loggedHours,
               p.endDate,
               s.name
        FROM ParticipationRecord pr
        JOIN pr.application a
        JOIN a.project p
        LEFT JOIN p.sdgs s
        WHERE a.employee.user.id = :userId
    """)
    List<Object[]> findRawCertificatesByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT YEAR(p.application.project.endDate), COALESCE(SUM(p.loggedHours), 0)
        FROM ParticipationRecord p
        WHERE p.application.project.endDate BETWEEN :startDate AND :endDate
        GROUP BY YEAR(p.application.project.endDate)
    """)
    List<Object[]> sumTotalHoursByYearRaw(
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

}