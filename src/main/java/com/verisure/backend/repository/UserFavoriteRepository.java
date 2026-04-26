package com.verisure.backend.repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.verisure.backend.entity.UserFavorite;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long>{

    Optional<UserFavorite> findByUserIdAndProjectId(Long userId, Long projectId);
    
    //Long countByProjectId(Long projectId);

    // Metodo para el dashboard:Contar likes en un rango de fechas para calcular la conversion
    @Query("""
        SELECT COUNT(u) 
        FROM UserFavorite u 
        WHERE u.createdAt BETWEEN :startDate AND :endDate
    """)
    Long countFavoritesByDateRange(
        @Param("startDate") OffsetDateTime startDate, 
        @Param("endDate") OffsetDateTime endDate
    );
}