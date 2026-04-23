package com.verisure.backend.seeder;

import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.verisure.backend.entity.EmployeeProfile;
import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.Role;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.EmployeeProfileRepository;
import com.verisure.backend.repository.GnoProfileRepository;
import com.verisure.backend.repository.ParticipationRecordRepository;
import com.verisure.backend.repository.ProjectRepository;
import com.verisure.backend.repository.SdgRepository;
import com.verisure.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class Seeder5YearsHistoric implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final GnoProfileRepository gnoProfileRepository;
    private final SdgRepository sdgRepository;
    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
    private final ParticipationRecordRepository participationRecordRepository;
    private final userFavoriteRepository userFavoriteRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("[SEEDER] La base de datos ya tiene información. Se omite la carga.");
            return;
        }

        log.info("[SEEDER] Iniciando carga masiva de datos para la DEMO...");

        seedGnos();
        seedEmployees();
        seedProjects();
        seedApplications();
        seedParticipationRecords();

        log.info("[SEEDER] Carga masiva completada con éxito. ¡Lista para la Demo!");
    }

    private final String[] gnoNames = {
        "Fundación Océanos Limpios", "Save The Forests", "Educa Sin Fronteras", 
        "Banco de Alimentos Central", "Refugio Animal Esperanza", "Tech For Good", 
        "Agua Viva ONG", "Sonrisas y Salud", "Raíces Verdes", "Mujeres en Acción"
    };

    private final String[] firstNames = {"Ana", "Carlos", "Laura", "David", "Elena", "Jorge", "Marta", "Luis", "Sara", "Pablo"};
    private final String[] lastNames = {"García", "Martínez", "López", "Sánchez", "Pérez", "Gómez", "Martín", "Ruiz", "Díaz", "Fernández"};
    private final String[] departments = {"RRHH", "IT", "Marketing", "Finanzas", "Operaciones", "Ventas", "Legal"};

    private final Random random = new Random();

    // ==========================================
    // FASE 1: CARGA DE USUARIOS Y PERFILES
    // ==========================================

    private void seedGnos() {
        log.info("[SEEDER] Generando 10 ONGs...");
        String defaultPassword = encoder.encode("Password123!");

        for (int i = 0; i < 10; i++) {
            User gnoUser = new User();
            gnoUser.setEmail("ong" + i + "@demo.com");
            gnoUser.setPasswordHash(defaultPassword);
            gnoUser.setRole(Role.ONG); // Asume que tienes un Enum Role.GNO
            userRepository.save(gnoUser);

            // 2. Crear el Perfil asociado
            GnoProfile profile = new GnoProfile();
            profile.setUser(gnoUser);
            profile.setOrganizationName(gnoNames[i]);
            // Añade aquí otros campos obligatorios que tenga tu entidad GnoProfile
            // profile.setDescription("Descripción de prueba para " + gnoNames[i]);
            
            gnoProfileRepository.save(profile);
        }
        log.info("[SEEDER] 10 ONGs generadas con éxito.");
    }

    private void seedEmployees() {
        log.info("[SEEDER] Generando 50 Empleados...");
        String defaultPassword = encoder.encode("Password123!");

        for (int i = 1; i <= 50; i++) {

            User employeeUser = new User();
            employeeUser.setEmail("empleado" + i + "@verisure.com");
            employeeUser.setPasswordHash(defaultPassword);
            employeeUser.setRole(Role.EMPLOYEE);
            userRepository.save(employeeUser);

            String randomFirstName = firstNames[random.nextInt(firstNames.length)];
            String randomLastName = lastNames[random.nextInt(lastNames.length)];
            String randomDept = departments[random.nextInt(departments.length)];

            EmployeeProfile profile = new EmployeeProfile();
            profile.setUser(employeeUser);
            profile.setFirstName(randomFirstName);
            profile.setLastName(randomLastName);
            profile.setDepartment(randomDept);
            // Añade aquí otros campos obligatorios que tenga tu entidad EmployeeProfile
            
            employeeProfileRepository.save(profile);
        } 
        log.info("[SEEDER] 50 Empleados generados con éxito.");
    }
}