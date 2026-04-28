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

        List<Project> findByGnoId(Long gnoId);

        List<Project> findByGnoIdOrderByCreatedAtDesc(Long gnoId);

        List<Project> findByStatus(StatusProject status);

        List<Project> findByStatusOrderByCreatedAtAsc(StatusProject status);

        List<Project> findByStatusAndCity(StatusProject status, String city);

        List<Project> findByStatusAndLocationType(StatusProject status, LocationType locationType);

        List<Project> findByStatusAndTitleContainingIgnoreCase(StatusProject status, String title);

        List<Project> findByStatusAndEndDateBefore(StatusProject status, OffsetDateTime date);

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

        @Query("""
                SELECT p AS project,
                        (SELECT COUNT(uf) FROM UserFavorite uf WHERE uf.project.id = p.id) AS favCount,
                        (SELECT COUNT(a) FROM Application a WHERE a.project.id = p.id AND a.status = :status) AS appCount
                FROM Project p
        """)
        List<ProjectAdminProjection> findAllWithCounts(@Param("status") StatusApplication status);

        @Query("""
                SELECT COUNT(p)
                FROM Project p
                WHERE p.status IN :statuses
                AND p.createdAt BETWEEN :startDate AND :endDate
        """)
        Long countProjectsByStatusesAndDateRange(
                        @Param("statuses") List<StatusProject> statuses,
                        @Param("startDate") OffsetDateTime startDate,
                        @Param("endDate") OffsetDateTime endDate);

        @Query("""
                SELECT COUNT(DISTINCT p.gno.id)
                FROM Project p
                WHERE p.status IN :statuses
                AND p.createdAt BETWEEN :startDate AND :endDate
        """)
        Long countDistinctGnosByProjectStatusesAndDateRange(
                        @Param("statuses") List<StatusProject> statuses,
                        @Param("startDate") OffsetDateTime startDate,
                        @Param("endDate") OffsetDateTime endDate);

        @Query("""
                SELECT COALESCE(SUM(p.requiredVolunteers), 0)
                FROM Project p
                WHERE p.status IN :statuses
                AND p.createdAt BETWEEN :startDate AND :endDate
        """)
        Long sumRequiredVolunteersByStatusesAndDateRange(
                        @Param("statuses") List<StatusProject> statuses,
                        @Param("startDate") OffsetDateTime startDate,
                        @Param("endDate") OffsetDateTime endDate);

        @Query("""
                SELECT COUNT(p.id)
                FROM Project p
                WHERE p.status IN :statuses
                AND p.endDate BETWEEN :startDate AND :endDate
        """)
        Long countProjectsByEndDate(
                        @Param("statuses") List<StatusProject> statuses,
                        @Param("startDate") OffsetDateTime startDate,
                        @Param("endDate") OffsetDateTime endDate);

        @Query("""
                SELECT YEAR(p.endDate), COUNT(p.id)
                FROM Project p
                WHERE p.status IN :statuses
                AND p.endDate BETWEEN :startDate AND :endDate
                GROUP BY YEAR(p.endDate)
        """)
        List<Object[]> countProjectsByYearRaw(
                @Param("statuses") List<StatusProject> statuses, 
                @Param("startDate") OffsetDateTime startDate, 
                @Param("endDate") OffsetDateTime endDate
        );
}