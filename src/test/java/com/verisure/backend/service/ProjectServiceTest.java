package com.verisure.backend.service;

import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.UserFavorite;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.repository.ProjectRepository;
import com.verisure.backend.repository.UserRepository;
import com.verisure.backend.repository.UserFavoriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFavoriteRepository userFavoriteRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    @DisplayName("Happy Path: Crea un nuevo favorito cuando no existía previamente")
    void toggleFavorite_AddLike_Success() {

        Long projectId = 1L;
        Long userId = 2L;

        Project project = new Project();
        project.setId(projectId);

        User user = new User();
        user.setId(userId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userFavoriteRepository.findByUserIdAndProjectId(userId, projectId)).thenReturn(Optional.empty());
        projectService.toggleFavorite(projectId, userId);

        verify(projectRepository).findById(projectId);
        verify(userRepository).findById(userId);
        verify(userFavoriteRepository).findByUserIdAndProjectId(userId, projectId);

        verify(userFavoriteRepository, times(1)).save(any(UserFavorite.class));
        verify(userFavoriteRepository, never()).delete(any(UserFavorite.class));
        //verify(userFavoriteRepository, times(1)).delete(any(UserFavorite.class));
    }

    @Test
    @DisplayName("Sad Path: Lanza ResourceNotFoundException desde getProjectOrThrow si el proyecto no existe")
    void toggleFavorite_ProjectNotFound() {

        Long projectId = 99L;
        Long userId = 2L;
        
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            projectService.toggleFavorite(projectId, userId);
        }, "Debería lanzar ResourceNotFoundException para un ID de proyecto inexistente");

        // assertThrows(NullPointerException.class, () -> {
        //     projectService.toggleFavorite(projectId, userId);
        // }, "Debería lanzar NullPointerException");

        verify(projectRepository).findById(projectId);
        verify(userRepository, never()).findById(anyLong());
        //verify(userRepository, times(1)).findById(anyLong());
        verify(userFavoriteRepository, never()).findByUserIdAndProjectId(anyLong(), anyLong());
    }
    
}