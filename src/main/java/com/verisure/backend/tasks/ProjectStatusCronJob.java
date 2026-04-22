package com.verisure.backend.tasks;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectStatusCronJob {

    private final ProjectRepository projectRepository;
    private final ApplicationService applicationService;

    // cron =[Segundos] [Minutos] [Horas] [Día del mes] [Mes] [Día de la semana]
    @Scheduled(cron = "*/10 * * * * *", zone = "Europe/Madrid") //Pruebas para ver la funcionalidad.

    // @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Madrid")
    @Transactional
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
            
            int certificados = applicationService.completeApplication(project.getId());
            
            log.info("🔒 Proyecto cerrado automáticamente: " + project.getTitle());
            //log.info("🎓 Empleados que han participado en este proyecto: " + certificados); todavia no hay certificados implementados
        }
        
        projectRepository.saveAll(expiredProjects);
        
        log.info("✅ [CRON] Revisión nocturna finalizada con éxito.");
    }
}