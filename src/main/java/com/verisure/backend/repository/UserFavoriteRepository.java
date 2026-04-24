package com.verisure.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.verisure.backend.entity.UserFavorite;

public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long>{

    Optional<UserFavorite> findByUserIdAndProjectId(Long userId, Long projectId);
    
    // Cuenta el total de "Likes" o interesados
    long countByProjectId(Long projectId);
}
