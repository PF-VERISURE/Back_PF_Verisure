// package com.verisure.backend.service;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.verisure.backend.dto.request.ApplicationRequestDTO;
// import com.verisure.backend.dto.response.EmployeeApplicationResponseDTO;
// import com.verisure.backend.entity.Application;
// import com.verisure.backend.entity.EmployeeProfile;
// import com.verisure.backend.entity.Project;
// import com.verisure.backend.entity.enums.StatusApplication;
// import com.verisure.backend.entity.enums.StatusProject;
// import com.verisure.backend.exception.DuplicateResourceException;
// import com.verisure.backend.exception.ResourceNotFoundException;
// import com.verisure.backend.exception.UnauthorizedActionException;
// import com.verisure.backend.mapper.ApplicationMapper;
// import com.verisure.backend.repository.ApplicationRepository;
// import com.verisure.backend.repository.EmployeeProfileRepository;
// import com.verisure.backend.repository.ProjectRepository;
// import com.verisure.backend.service.ApplicationService;

// @Service
// public class ApplicationServiceImpl implements ApplicationService {

//     private final ApplicationRepository applicationRepository;
//     private final ProjectRepository projectRepository;
//     private final EmployeeProfileRepository employeeProfileRepository;
//     private final ApplicationMapper applicationMapper;

//     public ApplicationServiceImpl(ApplicationRepository applicationRepository, 
//                                   ProjectRepository projectRepository,
//                                   EmployeeProfileRepository employeeProfileRepository,
//                                   ApplicationMapper applicationMapper) {
//         this.applicationRepository = applicationRepository;
//         this.projectRepository = projectRepository;
//         this.employeeProfileRepository = employeeProfileRepository;
//         this.applicationMapper = applicationMapper;
//     }

//     @Override
//     @Transactional
//     public EmployeeApplicationResponseDTO applyToProject(ApplicationRequestDTO request, Long userId) {
        
//         Project project = projectRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

//         EmployeeProfile employee = employeeProfileRepository.findByUserId(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Perfil de empleado no encontrado"));
//         if (applicationRepository.existsByProjectIdAndEmployeeId(project.getId(), employee.getId())) {
//             throw new DuplicateResourceException("Ya estás inscrito en este proyecto");
//         }

//         long currentApproved = applicationRepository.countProjectOccupancy(project.getId(), StatusApplication.APPROVED);

//         StatusApplication finalStatus = (currentApproved < project.getRequiredVolunteers()) 
//                 ? StatusApplication.APPROVED 
//                 : StatusApplication.WAITLISTED;

//         Application application = new Application();
//         application.setProject(project);
//         application.setEmployee(employee);
//         application.setStatus(finalStatus);
        
//         Application saved = applicationRepository.save(application);

//         return applicationMapper.toEmployeeResponse(saved);
//     }

//     @Override
//     @Transactional
//     public void cancelApplication(Long applicationId, Long userId) {
        
//         EmployeeProfile employee = employeeProfileRepository.findByUserId(userId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Perfil de empleado no encontrado"));

//         Application applicationToCancel = applicationRepository.findById(applicationId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada"));
//         if (!applicationToCancel.getEmployee().getId().equals(employee.getId())) {
//             throw new UnauthorizedActionException("No tienes permiso para cancelar esta inscripción");
//         }
//         if (applicationToCancel.getStatus() == StatusApplication.CANCELED) {
//             throw new IllegalStateException("La inscripción ya estaba cancelada");
//         }

//         boolean wasApproved = (applicationToCancel.getStatus() == StatusApplication.APPROVED);

//         applicationToCancel.setStatus(StatusApplication.CANCELED);
//         applicationRepository.save(applicationToCancel);

//         if (wasApproved) {
//             applicationRepository.findNextInWaitlist(applicationToCancel.getProject().getId(), StatusApplication.WAITLISTED)
//                 .ifPresent(nextInLine -> {
//                     nextInLine.setStatus(StatusApplication.APPROVED);
//                     applicationRepository.save(nextInLine);
                    
//                     // Aquí iría un servicio de Email/Notificaciones para avisarle, intentare el finde a ver como se gestiona.
//                     System.out.println("✅ Promoción FIFO ejecutada: El empleado ID " + 
//                                        nextInLine.getEmployee().getEmployeeId() + " ha conseguido plaza.");
//                 });
//         }
//     }

//     @Override
//     public List<EmployeeApplicationResponseDTO> getMyApplications(Long userId) {
//         // findEmployeeHistory
//         return null; 
//     }

//     @Override
//     @Transactional
//     public void completeApplication(Long applicationId) {
        
//     }

// }