package com.verisure.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.enums.StatusProject;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    //-----------Perfil ONG (crea)-----------/

    // Obtener todos los proyectos de una ONG
    List<Project> findByGnoId(Long gnoId);

    // Validar que un proyecto pertenece a una ONG (seguridad)
    boolean existsByIdAndGnoId(Long projectId, Long gnoId);

    // Obtener proyecto por id + ONG (para edición segura)
    Optional<Project> findByIdAndGnoId(Long projectId, Long gnoId);

    //--------Perfil ADMIN (valida y publica)--------/

    // Obtener proyectos por estado
    List<Project> findByStatus(StatusProject status);

    // Obtener proyectos pendientes
    List<Project> findByStatusOrderByCreatedAtAsc(StatusProject status);

    // Contar proyectos por estado (dashboard admin)
    long countProjectsByStatus(StatusProject status);

    //--------------Perfil EMPLEADO----------------/

    // Filtros básicos
    List<Project> findByStatusAndCity(StatusProject status, String city);

    List<Project> findByStatusAndLocationType(
        StatusProject status, 
        com.verisure.backend.entity.enums.LocationType locationType);

    // Búsqueda por título (tipo catálogo)
    List<Project> findByStatusAndTitleContainingIgnoreCase(StatusProject status, String title);

    //-------------Métricas-------------/

    // Conteo total por ONG
    long countByGnoId(Long gnoId);

    // Conteo por ONG y estado
    long countByGnoIdAndStatus(Long gnoId, StatusProject status);

    //------------------Optimización------

    // Traer proyecto con SDGs (evitar Lazy problems)
    @Query("""
        SELECT DISTINCT p FROM Project p
        LEFT JOIN FETCH p.sdgs
        WHERE p.id = :id
    """)
    Optional<Project> findByIdWithSdgs(Long id);
    
}
