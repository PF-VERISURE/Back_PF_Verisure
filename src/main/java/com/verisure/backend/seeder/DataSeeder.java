package com.verisure.backend.seeder;

import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.EmployeeProfile;
import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.Role;
import com.verisure.backend.entity.enums.StatusApplication;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.ProjectRepository;
import com.verisure.backend.repository.SdgRepository;
import com.verisure.backend.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SdgRepository sdgRepository;
    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
    private final BCryptPasswordEncoder encoder;

    public DataSeeder(UserRepository userRepository, SdgRepository sdgRepository, ProjectRepository projectRepository,
            ApplicationRepository applicationRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.sdgRepository = sdgRepository;
        this.projectRepository = projectRepository;
        this.applicationRepository = applicationRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // 1. SEEDER DE USUARIOS Y PERFILES
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@verisure.com");
            admin.setPasswordHash(encoder.encode("admin123"));
            admin.setRole(Role.ADMIN);

            EmployeeProfile adminProfile = new EmployeeProfile();
            adminProfile.setEmployeeId(999999L);
            adminProfile.setFirstName("Super");
            adminProfile.setLastName("Admin");
            adminProfile.setDepartment("Dirección General");
            adminProfile.setUser(admin);
            admin.setEmployeeProfile(adminProfile);
            userRepository.save(admin);

            String[] employeeNames = { "Laura", "Carlos", "Marta", "David", "Ana" };
            String[] employeeLastNames = { "Gómez", "Ruiz", "Sánchez", "López", "Martínez" };
            String[] departments = { "IT Support", "Ventas", "Marketing", "RRHH", "Finanzas" };
            Long[] empIds = { 100456L, 100457L, 100458L, 100459L, 100460L };

            for (int i = 0; i < 5; i++) {
                User employee = new User();
                employee.setEmail("empleado" + (i + 1) + "@verisure.com");
                employee.setPasswordHash(encoder.encode("user123"));
                employee.setRole(Role.EMPLOYEE);

                EmployeeProfile empProfile = new EmployeeProfile();
                empProfile.setEmployeeId(empIds[i]);
                empProfile.setFirstName(employeeNames[i]);
                empProfile.setLastName(employeeLastNames[i]);
                empProfile.setDepartment(departments[i]);
                empProfile.setUser(employee);
                employee.setEmployeeProfile(empProfile);
                userRepository.save(employee);
            }

            String[] ongNames = { "Cruz Roja Verisure", "Médicos Sin Fronteras", "Save the Children" };
            String[] cifs = { "G12345678", "G87654321", "G11223344" };
            String[] emails = { "cruzroja", "msf", "savethechildren" };

            for (int i = 0; i < 3; i++) {
                User ong = new User();
                ong.setEmail(emails[i] + "@verisure.com");
                ong.setPasswordHash(encoder.encode("ong123"));
                ong.setRole(Role.ONG);

                GnoProfile ongProfile = new GnoProfile();
                ongProfile.setCif(cifs[i]);
                ongProfile.setOrganizationName(ongNames[i]);
                ongProfile.setContactName("Contacto " + (i + 1));
                ongProfile.setContactPhone("+3460011223" + i);
                ongProfile.setContactEmail("info@" + emails[i] + ".org");
                ongProfile.setWebsite("https://www." + emails[i] + ".org");
                ongProfile.setAddress("Calle Principal " + (i + 1) + ", Madrid");
                ongProfile.setUser(ong);
                ong.setGnoProfile(ongProfile);
                userRepository.save(ong);
            }
            System.out.println("✅ Usuarios y ONGs inicializados.");
        }

        // 2. SEEDER DE ODS 
        if (sdgRepository.count() == 0) {
            List<Sdg> sdgs = List.of(
                    createSdg(1, "Fin de la pobreza"), createSdg(2, "Hambre cero"),
                    createSdg(3, "Salud y bienestar"), createSdg(5, "Igualdad de género"),
                    createSdg(6, "Agua limpia y saneamiento"), createSdg(7, "Energía asequible"),
                    createSdg(9, "Industria e innovación"), createSdg(10, "Reducción desigualdades"),
                    createSdg(11, "Ciudades sostenibles"), createSdg(12, "Consumo responsable"));
            sdgRepository.saveAll(sdgs);
            System.out.println("✅ ODS inicializados.");
        }

        // 3. SEEDER DE PROYECTOS 
        if (projectRepository.count() == 0) {
            List<User> ongs = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.ONG).toList();

            // Proyecto Futuro 1: Cruz Roja
            Project p1 = createProject("Apoyo Escolar Digital", "Descripción...", "url", 2, LocationType.ONLINE, "url", "Madrid", "Horas", OffsetDateTime.now().plusDays(5), OffsetDateTime.now().plusMonths(2), 40, List.of(1, 4));
            p1.setGno(ongs.get(0).getGnoProfile());
            p1.setStatus(StatusProject.PUBLISHED);

            // Proyecto Futuro 2: MSF
            Project p2 = createProject("Material Sanitario", "Descripción...", "url", 1, LocationType.IN_PERSON, "calle", "Barcelona", "Cajas", OffsetDateTime.now().plusDays(10), OffsetDateTime.now().plusMonths(1), 20, List.of(3));
            p2.setGno(ongs.get(1).getGnoProfile());
            p2.setStatus(StatusProject.PUBLISHED);

            // Proyecto CADUCADO (DEBE CERRARSE)
            Project p_caducado = createProject("Reforestación Test Cron", "Fabricado con % relacionado.", "url", 5, LocationType.IN_PERSON, "Monte", "Madrid", "Árboles", OffsetDateTime.now().minusMonths(1), OffsetDateTime.now().minusDays(2), 15, List.of(12));
            p_caducado.setGno(ongs.get(0).getGnoProfile());
            p_caducado.setStatus(StatusProject.PUBLISHED);

            projectRepository.saveAll(List.of(p1, p2, p_caducado));
            System.out.println("✅ Proyectos creados (incluyende el test [CRON]).");
        }

        if (applicationRepository.count() == 0) {
            Project expired = projectRepository.findByStatusAndTitleContainingIgnoreCase(StatusProject.PUBLISHED, "Reforestación").get(0);
            Project active = projectRepository.findByStatusAndTitleContainingIgnoreCase(StatusProject.PUBLISHED, "Escolar").get(0);
            List<User> emps = userRepository.findAll().stream().filter(u -> u.getRole() == Role.EMPLOYEE).toList();

            createApp(expired, emps.get(0), StatusApplication.APPROVED);   
            createApp(expired, emps.get(1), StatusApplication.APPROVED);
            createApp(expired, emps.get(2), StatusApplication.WAITLISTED);
            createApp(expired, emps.get(3), StatusApplication.PENDING);
            createApp(expired, emps.get(4), StatusApplication.CANCELED);

            createApp(active, emps.get(0), StatusApplication.APPROVED);

            System.out.println("✅ Inscripciones creadas para testear todos los flujos.");
        }
    }

    private void createApp(Project p, User u, StatusApplication s) {
        Application app = new Application();
        app.setProject(p);
        app.setEmployee(u.getEmployeeProfile());
        app.setStatus(s);
        applicationRepository.save(app);
    }

    private Sdg createSdg(Integer id, String name) {
        Sdg sdg = new Sdg();
        sdg.setId(id);
        sdg.setName(name);
        return sdg;
    }

    private Project createProject(String title, String description, String imageUrl, Integer requiredVolunteers,
            LocationType locationType, String address, String city, String impactUnit, OffsetDateTime startDate,
            OffsetDateTime endDate, Integer totalHours, List<Integer> sdgIds) {
        Project project = new Project();
        project.setTitle(title);
        project.setDescription(description);
        project.setImageUrl(imageUrl);
        project.setRequiredVolunteers(requiredVolunteers);
        project.setLocationType(locationType);
        project.setAddress(address);
        project.setCity(city);
        project.setImpactUnit(impactUnit);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        project.setTotalHours(totalHours);
        project.setSdgs(sdgRepository.findAllById(sdgIds));
        return project;
    }
}

// Vamos a ir ampliando el seeder segun se vayan creando las entidades y los servicios.