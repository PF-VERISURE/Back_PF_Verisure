package com.verisure.backend.service;

import com.verisure.backend.dto.request.ApplicationRequestDTO;
import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;
import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.EmployeeProfile;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.StatusApplication;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.exception.BadRequestException;
import com.verisure.backend.mapper.ApplicationMapper;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.EmployeeProfileRepository;
import com.verisure.backend.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeProfileRepository employeeProfileRepository;

    @Mock
    private ParticipationRecordService participationRecordService;

    @Mock
    private ApplicationMapper applicationMapper;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    @DisplayName("Happy Path: Crea una solicitud APPROVED si hay plazas y el proyecto está publicado")
    void applyToProject_Success_Approved() {
        
        Long projectId = 1L;
        Long userId = 2L;
        Long employeeId = 10L;

        ApplicationRequestDTO request = new ApplicationRequestDTO(projectId);

        Project project = new Project();
        project.setId(projectId);
        project.setStatus(StatusProject.PUBLISHED);
        project.setRequiredVolunteers(5);

        EmployeeProfile employee = new EmployeeProfile();
        employee.setId(employeeId);

        EmployeeApplicationResponseDTO expectedResponse = new EmployeeApplicationResponseDTO(
                100L, 
                StatusApplication.APPROVED, 
                OffsetDateTime.now(), 
                projectId, 
                "Proyecto Reforestación", 
                OffsetDateTime.now().plusDays(1), 
                OffsetDateTime.now().plusDays(5), 
                LocationType.IN_PERSON,
                "http://imagen.url", 
                10, 
                List.of("Fin de la pobreza", "Acción por el clima")
        );

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(employeeProfileRepository.findByUserId(userId)).thenReturn(Optional.of(employee));
        when(applicationRepository.findByProjectIdAndEmployeeId(projectId, employeeId)).thenReturn(Optional.empty());
        when(applicationRepository.countByProjectIdAndStatus(projectId, StatusApplication.APPROVED)).thenReturn(2L);
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(applicationMapper.toEmployeeResponse(any(Application.class))).thenReturn(expectedResponse);

        EmployeeApplicationResponseDTO result = applicationService.applyToProject(request, userId);

        assertNotNull(result, "La respuesta no debería ser nula");
        assertEquals(StatusApplication.APPROVED, result.status(), "El estado debería ser APPROVED");
        
        verify(projectRepository).findById(projectId);
        verify(employeeProfileRepository).findByUserId(userId);
        verify(applicationRepository).findByProjectIdAndEmployeeId(projectId, employeeId);
        verify(applicationRepository, times(1)).save(any(Application.class));
        // verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    @DisplayName("Sad Path: Lanza BadRequestException si el proyecto no está publicado")
    void applyToProject_ProjectNotPublished() {
        
        Long projectId = 1L;
        Long userId = 2L;

        ApplicationRequestDTO request = new ApplicationRequestDTO(projectId);

        Project project = new Project();
        project.setId(projectId);
        project.setStatus(StatusProject.PENDING);

        EmployeeProfile employee = new EmployeeProfile();
        employee.setId(10L);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(employeeProfileRepository.findByUserId(userId)).thenReturn(Optional.of(employee));

        assertThrows(BadRequestException.class, () -> {
            applicationService.applyToProject(request, userId);
        }, "Debería lanzar BadRequestException si el proyecto no es PUBLISHED");

        verify(applicationRepository, never()).findByProjectIdAndEmployeeId(anyLong(), anyLong());
        verify(applicationRepository, never()).save(any(Application.class));
        // verify(applicationRepository, times(1)).save(any(Application.class));
    }
}