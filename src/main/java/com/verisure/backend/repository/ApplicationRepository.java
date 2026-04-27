package com.verisure.backend.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.enums.StatusApplication;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    //query para obtener las aplicaciones de un proyecto: 
    // SELECT * FROM applications WHERE project_id = ?
    List<Application> findByProjectId(Long projectId);

    //query para verificar si un empleado tiene una aplicacion en un proyecto: 
    // SELECT * FROM applications WHERE project_id = ? AND employee_id = ?
    // boolean existsByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    //query para obtener una aplicacion de un proyecto: 
    // SELECT * FROM applications WHERE project_id = ? AND employee_id = ?
    Optional<Application> findByProjectIdAndEmployeeId(Long projectId, Long employeeId);

    //query para obtener las aplicaciones de un proyecto con un estado especifico: 
    // SELECT * FROM applications WHERE project_id = ? AND status = ?
    // List<Application> findByProjectIdAndStatus(Long projectId, StatusApplication status);

    //query para obtener todas las aplicaciones ordenadas por fecha de creacion: OJO! Deberia tener Pegeable para craga masiva.
    // SELECT * FROM applications ORDER BY created_at DESC
    List<Application> findAllByOrderByCreatedAtDesc();

    //query para obtener el historial de aplicaciones de un empleado: 
    // @Query("""
    //     SELECT a 
    //     FROM Application a 
    //     WHERE a.employee.id = :employeeId 
    //     ORDER BY a.createdAt DESC
    // """)
    //List<Application> findEmployeeHistory(@Param("employeeId") Long employeeId);
    List<Application> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    //query para obtener la cantidad de aplicaciones por proyecto: 
    // @Query("""
    //     SELECT COUNT(a) 
    //     FROM Application a 
    //     WHERE a.project.id = :projectId 
    //     AND a.status = :status
    // """)
    // long countProjectOccupancy(@Param("projectId") Long projectId, @Param("status") StatusApplication status);
    Long countByProjectIdAndStatus(Long projectId, StatusApplication status);

    //query para obtener la siguiente aplicacion en la lista de espera: 
    @Query("""
        SELECT a 
        FROM Application a 
        WHERE a.project.id = :projectId 
        AND a.status = :status
        ORDER BY a.createdAt ASC 
        LIMIT 1
    """)
    Optional<Application> findNextInWaitlist(@Param("projectId") Long projectId, @Param("status") StatusApplication status);

    //query para obtener la cantidad de aplicaciones por categoria del SDG para la grafica del DONUT numero 2 ANTIGUO
    // @Query("""
    //     SELECT s.name, COUNT(a.id)
    //     FROM Application a
    //     JOIN a.project p
    //     JOIN p.sdgs s
    //     WHERE a.createdAt BETWEEN :startDate AND :endDate
    //     GROUP BY s.name
    // """)
    // List<Object[]> countApplicationsByCategoryRaw(
    //     @Param("startDate") OffsetDateTime startDate, 
    //     @Param("endDate") OffsetDateTime endDate
    // );

    // Para la tarjeta VOLUNTARIOS ACTIVOS: Cuenta IDs de empleados únicos que estén aprobados VolunteersKpiResponseDTO
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

    // Cuenta el total de solicitudes exitosas (APPROVED o CLOSED), contar el "volumen bruto" de solicitudes exitosas para poder dividirlo entre los Likes y sacar el porcentaje de conversión. ConversionKpiResponseDTO
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

    // NUEVO: Cuenta voluntarios únicos basados en la fecha en la que terminó el proyecto
    @Query("""
        SELECT COUNT(DISTINCT a.employee) 
        FROM Application a 
        WHERE a.status IN :statuses 
        AND a.project.endDate BETWEEN :startDate AND :endDate
    """)
    Long countDistinctVolunteersByProjectEndDate(
        @Param("statuses") List<StatusApplication> statuses, 
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );

    // Agrupa por el nombre del SDG, cuenta cuántas aplicaciones (APPROVED/CLOSED) tiene, y ordena para que el más popular quede primero.
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
}