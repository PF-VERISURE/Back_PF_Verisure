package com.verisure.backend.repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.ParticipationRecord;

@Repository
public interface ParticipationRecordRepository extends JpaRepository<ParticipationRecord, Long> {

    //query para obtener el total de horas participadas en un rango de fechas:
    @Query("""
        SELECT COALESCE(SUM(p.loggedHours), 0) 
        FROM ParticipationRecord p 
        WHERE p.createdAt BETWEEN :startDate AND :endDate
    """)
    BigDecimal sumTotalHoursByDateRange(
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    // NUEVO: Suma de horas basadas en la fecha en la que terminó el proyecto
    @Query("""
        SELECT COALESCE(SUM(p.loggedHours), 0) 
        FROM ParticipationRecord p 
        WHERE p.application.project.endDate BETWEEN :startDate AND :endDate
    """)
    BigDecimal sumTotalHoursByProjectEndDate(
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

}