package com.verisure.backend.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.verisure.backend.dto.response.CategoryCountResponseDTO;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.StatusProject;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // -----------Perfil ONG (crea)-----------/

    // Obtener todos los proyectos de una ONG
    List<Project> findByGnoId(Long gnoId);

    // Validar que un proyecto pertenece a una ONG (seguridad)
    boolean existsByIdAndGnoId(Long projectId, Long gnoId);

    // Obtener proyecto por id + ONG (para edición segura)
    Optional<Project> findByIdAndGnoId(Long projectId, Long gnoId);

    // --------Perfil ADMIN (valida y publica)--------/

    // Obtener proyectos por estado
    List<Project> findByStatus(StatusProject status);

    // Obtener proyectos pendientes
    List<Project> findByStatusOrderByCreatedAtAsc(StatusProject status);

    // Contar proyectos por estado (dashboard admin)
    long countProjectsByStatus(StatusProject status);

    // --------------Perfil EMPLEADO----------------/

    // Filtros básicos
    List<Project> findByStatusAndCity(StatusProject status, String city);

    List<Project> findByStatusAndLocationType(
            StatusProject status,
            LocationType locationType);

    // Búsqueda por título (tipo catálogo)
    List<Project> findByStatusAndTitleContainingIgnoreCase(StatusProject status, String title);

    // ------------------Optimización------

    // Traer proyecto con SDGs (evitar Lazy problems)
    @Query("""
                SELECT DISTINCT p FROM Project p
                LEFT JOIN FETCH p.sdgs
                WHERE p.id = :id
            """)

    Optional<Project> findByIdWithSdgs(Long id);

    // Para el filtrado del Cron Job
    List<Project> findByStatusAndEndDateBefore(StatusProject status, OffsetDateTime date);

    // -------------Métricas-------------/

    // Conteo total por ONG
    long countByGnoId(Long gnoId);

    // Conteo por ONG y estado
    long countByGnoIdAndStatus(Long gnoId, StatusProject status);

    // Grafica 1: donut registro de proyectos por categoria con filtro de año o mes.
    @Query("""
                SELECT s.name, COUNT(p.id)
                FROM Project p
                JOIN p.sdgs s
                WHERE p.startDate BETWEEN :startDate AND :endDate
                GROUP BY s.name
            """)
    List<Object[]> countRegisteredProjectsRaw(
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate);

}