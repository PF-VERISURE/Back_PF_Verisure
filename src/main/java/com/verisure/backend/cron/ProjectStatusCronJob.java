package com.verisure.backend.cron;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.enums.StatusApplication;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.ProjectRepository;
import com.verisure.backend.service.ApplicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectStatusCronJob {

    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;

    // cron =[Segundos] [Minutos] [Horas] [Día del mes] [Mes] [Día de la semana]
    // @Scheduled(cron = "*/10 * * * * *", zone = "Europe/Madrid") //Pruebas para ver la funcionalidad.

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Madrid")
    public void executeNightlyProjectClosure() {

        log.info("🌙 [CRON] Iniciando revisión de proyectos caducados a medianoche...");

        List<Project> expiredProjects = projectRepository.findByStatusAndEndDateBefore(
                StatusProject.PUBLISHED, 
                OffsetDateTime.now()
        );

        if (expiredProjects.isEmpty()) {
            log.info("✅ [CRON] No hay proyectos para cerrar hoy.");
            return;
        }

        for (Project project : expiredProjects) {
            project.setStatus(StatusProject.COMPLETED); 
            projectRepository.save(project);
            log.info("🔒 Proyecto cerrado automáticamente: " + project.getTitle());

            List<Application> approvedApps = applicationRepository.findByProjectIdAndStatus(
                    project.getId(), 
                    StatusApplication.APPROVED
            );

            for (Application app : approvedApps) {
                applicationService.completeApplication(app.getId());
            }
            
            log.info("🎓 Empleados que han participado en este proyecto: " + approvedApps.size());
        }
        
        log.info("✅ [CRON] Revisión nocturna finalizada con éxito.");
    }
}