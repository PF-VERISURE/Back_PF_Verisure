package com.verisure.backend.config;

import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.EmployeeProfile;
import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.Role;
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

        // 1. SEEDER DE USUARIOS Y PERFILES ASOCIADOS
        if (userRepository.count() == 0) {

            // ==========================================
            // ADMINISTRADOR
            // ==========================================
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

            // ==========================================
            // EMPLEADOS (Simulando carga de RRHH)
            // ==========================================
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

            // ==========================================
            // ONGS (GNO Profiles)
            // ==========================================
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

            System.out.println("✅ Administrador, Empleados y ONGs creados con éxito.");
        }

        // 2. SEEDER DE ODS
        if (sdgRepository.count() == 0) {
            List<Sdg> sdgs = List.of(
                    createSdg(1, "Fin de la pobreza"),
                    createSdg(2, "Hambre cero"),
                    createSdg(3, "Salud y bienestar"),
                    createSdg(5, "Igualdad de género"),
                    createSdg(6, "Agua limpia y saneamiento"),
                    createSdg(7, "Energía asequible y no contaminante"),
                    createSdg(9, "Industria, innovación e infraestructura"),
                    createSdg(10, "Reducción de las desigualdades"),
                    createSdg(11, "Ciudades y comunidades sostenibles"),
                    createSdg(12, "Producción y consumo responsables"));
            sdgRepository.saveAll(sdgs);
            System.out.println("✅ Catálogo de ODS (SDGs) inicializado.");
        }

        // 3. SEEDER DE PROYECTOS
        if (projectRepository.count() == 0) {
            List<User> ongs = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.ONG)
                    .toList();

            if (ongs.size() >= 3) {
                // Proyecto 1: Cruz Roja - ONLINE
                Project p1 = createProject(
                        "Apoyo Escolar Digital",
                        "Clases de refuerzo online para niños en riesgo de exclusión. Fabricado con % relacionado.",
                        "https://images.unsplash.com/photo-1503676260728-1c00da094a0b",
                        2,
                        LocationType.ONLINE,
                        null, "Madrid", "Horas de clase impartidas",
                        OffsetDateTime.now().plusDays(5), OffsetDateTime.now().plusMonths(2), 40,
                        List.of(1, 4));
                p1.setGno(ongs.get(0).getGnoProfile());
                p1.setStatus(StatusProject.PUBLISHED);

                // Proyecto 2: MSF - IN_PERSON
                Project p2 = createProject(
                        "Clasificación de Material Sanitario",
                        "Apoyo presencial en la organización de suministros médicos de primera necesidad. Fabricado con % relacionado.",
                        "https://images.unsplash.com/photo-1584515933487-779824d29309",
                        1,
                        LocationType.IN_PERSON,
                        "Calle Solidaridad 123", "Barcelona", "Cajas organizadas",
                        OffsetDateTime.now().plusDays(10), OffsetDateTime.now().plusMonths(1), 20,
                        List.of(3));
                p2.setGno(ongs.get(1).getGnoProfile());
                p2.setStatus(StatusProject.PUBLISHED);

                // Proyecto 3: Save the Children - ONLINE
                Project p3 = createProject(
                        "Mentoring para Jóvenes",
                        "Programa de mentoría a distancia para el desarrollo personal y profesional juvenil. Fabricado con % relacionado.",
                        "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c",
                        3,
                        LocationType.ONLINE,
                        null, "Sevilla", "Sesiones completadas",
                        OffsetDateTime.now().plusDays(15), OffsetDateTime.now().plusMonths(3), 60,
                        List.of(10));
                p3.setGno(ongs.get(2).getGnoProfile());

                projectRepository.saveAll(List.of(p1, p2, p3));
                System.out.println("✅ Proyectos de prueba creados y asignados a ONGs.");
                Project p4_caducado = createProject(
                        "Reforestación Invernal (Test Cron)",
                        "Proyecto de plantación de árboles en zonas afectadas. Fabricado con % relacionado.",
                        "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09",
                        2,
                        LocationType.IN_PERSON,
                        "Monte Abedul", "Madrid", "Árboles plantados",
                        OffsetDateTime.now().minusDays(30),
                        OffsetDateTime.now().minusDays(2),
                        15,
                        List.of(15));
                p4_caducado.setGno(ongs.get(0).getGnoProfile());
                p4_caducado.setStatus(StatusProject.PUBLISHED);
                projectRepository.save(p4_caducado);
                System.out.println("⏳ Proyecto fantasma (caducado) creado para pruebas.");

            }
        } 

        if (applicationRepository.count() == 0) {

            Project expiredProject = projectRepository
                    .findByStatusAndTitleContainingIgnoreCase(StatusProject.PUBLISHED, "Reforestación").get(0);
            List<User> employees = userRepository.findAll().stream()
                    .filter(u -> u.getRole() == Role.EMPLOYEE)
                    .toList();

            Application app1 = new Application();
            app1.setProject(expiredProject);
            app1.setEmployee(employees.get(0).getEmployeeProfile());
            app1.setStatus(com.verisure.backend.entity.enums.StatusApplication.APPROVED);
            applicationRepository.save(app1);

            Application app2 = new Application();
            app2.setProject(expiredProject);
            app2.setEmployee(employees.get(1).getEmployeeProfile());
            app2.setStatus(com.verisure.backend.entity.enums.StatusApplication.APPROVED);
            applicationRepository.save(app2);

            Application app3 = new Application();
            app3.setProject(expiredProject);
            app3.setEmployee(employees.get(2).getEmployeeProfile());
            app3.setStatus(com.verisure.backend.entity.enums.StatusApplication.WAITLISTED);
            applicationRepository.save(app3);

            System.out.println("✅ Inscripciones de prueba creadas (2 Aprobados, 1 En Espera).");
        }
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

        List<Sdg> sdgs = sdgRepository.findAllById(sdgIds);
        project.setSdgs(sdgs);

        return project;
    }
}

// Vamos a ir ampliando el seeder segun se vayan creando las entidades y los
// servicios.