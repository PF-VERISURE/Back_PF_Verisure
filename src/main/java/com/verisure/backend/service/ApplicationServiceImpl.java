package com.verisure.backend.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.verisure.backend.dto.request.ApplicationRequestDTO;
import com.verisure.backend.dto.response.AdminApplicationListResponseDTO;
import com.verisure.backend.dto.response.AdminApplicationResponseDTO;
import com.verisure.backend.dto.response.EmployeeApplicationListResponseDTO;
import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;
import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.EmployeeProfile;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.enums.StatusApplication;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.exception.BadRequestException;
import com.verisure.backend.exception.DuplicateResourceException;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.exception.UnauthorizedActionException;
import com.verisure.backend.mapper.ApplicationMapper;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.EmployeeProfileRepository;
import com.verisure.backend.repository.ProjectRepository;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final ParticipationRecordService participationRecordService;    
    private final ApplicationMapper applicationMapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
            ProjectRepository projectRepository,
            EmployeeProfileRepository employeeProfileRepository,
            ParticipationRecordService participationRecordService,
            ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.projectRepository = projectRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.participationRecordService = participationRecordService;
        this.applicationMapper = applicationMapper;
    }

    // ==========================================
    // ADMIN
    // ==========================================

    // ver todas las inscripciones orderby desc
    @Override
    @Transactional(readOnly = true)
    public AdminApplicationListResponseDTO getAllApplications() {
        List<Application> applications = applicationRepository.findAllByOrderByCreatedAtDesc();
        List<AdminApplicationResponseDTO> listApplications = applicationMapper.toAdminListResponse(applications);
        return new AdminApplicationListResponseDTO(listApplications, listApplications.size());
    }

    // ==========================================
    // EMPLOYEE
    // ==========================================

    // inscribirse a un proyecto
    @Override
    @Transactional
    public EmployeeApplicationResponseDTO applyToProject(ApplicationRequestDTO request, Long userId) {

        Project project = getProjectById(request.projectId());
        EmployeeProfile employee = getEmployeeByUserId(userId);

        if (project.getStatus() != StatusProject.PUBLISHED) {
            throw new BadRequestException("No puedes inscribirte: el proyecto no está publicado.");
        }

        Optional<Application> existingApplication = applicationRepository.findByProjectIdAndEmployeeId(project.getId(),
                employee.getId());

        Application application;

        if (existingApplication.isPresent()) {
            application = existingApplication.get();
            if (application.getStatus() != StatusApplication.CANCELED) {
                throw new DuplicateResourceException("Ya estás inscrito en este proyecto");
            }
        } else {
            application = new Application();
            application.setProject(project);
            application.setEmployee(employee);
        }

        long currentApproved = applicationRepository.countProjectOccupancy(project.getId(), StatusApplication.APPROVED);
        StatusApplication finalStatus = (currentApproved < project.getRequiredVolunteers())
                ? StatusApplication.APPROVED
                : StatusApplication.WAITLISTED;

        application.setStatus(finalStatus);

        Application saved = applicationRepository.save(application);
        return applicationMapper.toEmployeeResponse(saved);
    }

    // cancelar inscripción
    @Override
    @Transactional
    public void cancelApplication(Long applicationId, Long userId) {

        EmployeeProfile employee = getEmployeeByUserId(userId);

        Application applicationToCancel = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));

        if (!applicationToCancel.getEmployee().getId().equals(employee.getId())) {
            throw new UnauthorizedActionException("No tienes permiso para cancelar esta inscripción");
        }
        if (applicationToCancel.getStatus() == StatusApplication.CANCELED) {
            throw new IllegalStateException("La inscripción ya estaba cancelada");
        }

        boolean wasApproved = (applicationToCancel.getStatus() == StatusApplication.APPROVED);

        applicationToCancel.setStatus(StatusApplication.CANCELED);
        applicationRepository.save(applicationToCancel);

        if (wasApproved) {
            applicationRepository
                    .findNextInWaitlist(applicationToCancel.getProject().getId(), StatusApplication.WAITLISTED)
                    .ifPresent(nextInLine -> {
                        nextInLine.setStatus(StatusApplication.APPROVED);
                        applicationRepository.save(nextInLine);

                        // Aquí iría un servicio de notificaciones, averiguar bien SSE Server-Sent
                        // Events.NO MVP
                        System.out.println("✅ Promoción FIFO ejecutada: El empleado ID " +
                                nextInLine.getEmployee().getEmployeeId() + " ha conseguido plaza.");
                    });
        }
    }

    // ver sus inscripciones
    @Override
    @Transactional(readOnly = true)
    public EmployeeApplicationListResponseDTO getMyApplications(Long userId) {
        EmployeeProfile employee = getEmployeeByUserId(userId);
        List<Application> applications = applicationRepository.findEmployeeHistory(employee.getId());
        List<EmployeeApplicationResponseDTO> listApplications = applicationMapper.toEmployeeListResponse(applications);
        return new EmployeeApplicationListResponseDTO(listApplications, listApplications.size());
    }

    // ==========================================
    // AUTOMATIZACIÓN
    // ==========================================

    // cronjob para finalizar proyectos de todo los estados
    @Override
    @Transactional
    public int completeApplication(Long projectId) {
        List<Application> applications = applicationRepository.findByProjectId(projectId);

        int closedCount = 0;
        int rejectedCount = 0; //no para MVP ni Sprint 3

        for (Application app : applications) {
            switch (app.getStatus()) {
                case APPROVED:
                    app.setStatus(StatusApplication.CLOSED);
                    participationRecordService.createParticipationRecord(app);
                    closedCount++;
                    break;

                case WAITLISTED:
                case PENDING:
                    app.setStatus(StatusApplication.REJECTED);
                    rejectedCount++;
                    break;

                case CANCELED:
                case REJECTED:
                case CLOSED:
                    break;
            }
        }

        applicationRepository.saveAll(applications);

        return closedCount;
    }

    // metodos privados para DRY

    private EmployeeProfile getEmployeeByUserId(Long userId) {
        return employeeProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de empleado no encontrado"));
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
    }

}