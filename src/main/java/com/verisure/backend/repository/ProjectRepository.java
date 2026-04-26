package com.verisure.backend.repository;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.StatusApplication;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.repository.projection.ProjectAdminProjection;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

        // que me traiga todos los proyectos de una ONG
        // @Query("SELECT p FROM Project p WHERE p.gnoId = :gnoId")
        List<Project> findByGnoId(Long gnoId);

        // validar que un proyecto pertenece a una ONG
        // @Query("SELECT COUNT(p) > 0 FROM Project p WHERE p.id = :projectId AND p.gnoId = :gnoId")
        //boolean existsByIdAndGnoId(Long projectId, Long gnoId);

        // obtener proyecto por id + ONG
        // @Query("SELECT p FROM Project p WHERE p.id = :projectId AND p.gnoId = :gnoId")
        //Optional<Project> findByIdAndGnoId(Long projectId, Long gnoId);

        // obtener todos los proyectos por estado
        // @Query("SELECT p FROM Project p WHERE p.status = :status")
        List<Project> findByStatus(StatusProject status);

        // obtener todos los proyectos por estado ordenados por fecha de creación
        // @Query("SELECT p FROM Project p WHERE p.status = :status ORDER BY p.createdAt ASC")
        List<Project> findByStatusOrderByCreatedAtAsc(StatusProject status);

        // contar proyectos por estado
        // @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
        // long countProjectsByStatus(StatusProject status);

        // obtener todos los proyectos por estado y ciudad
        // @Query("SELECT p FROM Project p WHERE p.status = :status AND p.city = :city")
        List<Project> findByStatusAndCity(StatusProject status, String city);

        // obtener todos los proyectos por estado y tipo de ubicación
        // @Query("SELECT p FROM Project p WHERE p.status = :status AND p.locationType = :locationType")
        List<Project> findByStatusAndLocationType(StatusProject status, LocationType locationType);

        // obtener todos los proyectos por estado y título
        // @Query("SELECT p FROM Project p WHERE p.status = :status AND p.title LIKE %:title%")
        List<Project> findByStatusAndTitleContainingIgnoreCase(StatusProject status, String title);

        // obtener proyecto por id con sus SDG
        // @Query("""
        //         SELECT DISTINCT p FROM Project p
        //         LEFT JOIN FETCH p.sdgs
        //         WHERE p.id = :id
        // """)
        // Optional<Project> findByIdWithSdgs(Long id);

        // obtener todos los proyectos por estado y fecha de finalización
        // @Query("SELECT p FROM Project p WHERE p.status = :status AND p.endDate < :date")
        List<Project> findByStatusAndEndDateBefore(StatusProject status, OffsetDateTime date);

        // contar proyectos por ONG
        // @Query("SELECT COUNT(p) FROM Project p WHERE p.gnoId = :gnoId")
        //long countByGnoId(Long gnoId);

        // contar proyectos por ONG y estado
        // @Query("SELECT COUNT(p) FROM Project p WHERE p.gnoId = :gnoId AND p.status = :status")
        //long countByGnoIdAndStatus(Long gnoId, StatusProject status);

        // contar proyectos por categoría
        // para la grafica del DONUT umero 1
        @Query("""
                SELECT s.name, COUNT(p.id)
                FROM Project p
                JOIN p.sdgs s
                WHERE p.startDate BETWEEN :startDate AND :endDate
                GROUP BY s.name
        """)
        List<Object[]> countProjectsByCategoryRaw(
                        @Param("startDate") OffsetDateTime startDate,
                        @Param("endDate") OffsetDateTime endDate);

        // consultas anidadas que usan proyecciones para obtener los proyectos con sus conteos de favoritos y aplicaciones evitando el problema de n+1              
        @Query("""
                SELECT p AS project,
                        (SELECT COUNT(uf) FROM UserFavorite uf WHERE uf.project.id = p.id) AS favCount,
                        (SELECT COUNT(a) FROM Application a WHERE a.project.id = p.id AND a.status = :status) AS appCount
                FROM Project p
        """)
        List<ProjectAdminProjection> findAllWithCounts(@Param("status") StatusApplication status);
}