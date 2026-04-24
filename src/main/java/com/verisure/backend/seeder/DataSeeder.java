package com.verisure.backend.seeder;

import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.EmployeeProfile;
import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.ParticipationRecord;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.Role;
import com.verisure.backend.entity.enums.StatusApplication;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.EmployeeProfileRepository;
import com.verisure.backend.repository.GnoProfileRepository;
import com.verisure.backend.repository.ParticipationRecordRepository;
import com.verisure.backend.repository.ProjectRepository;
import com.verisure.backend.repository.SdgRepository;
import com.verisure.backend.repository.UserFavoriteRepository;
import com.verisure.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final GnoProfileRepository gnoProfileRepository;
    private final SdgRepository sdgRepository;
    private final ProjectRepository projectRepository;
    private final ApplicationRepository applicationRepository;
    private final ParticipationRecordRepository participationRecordRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            return;
        }

        seedGnos();
        seedEmployees();
        seedSdgs();
        seedProjects();
        seedApplicationsAndRecords();
    }

    private final String[] gnoNames = {
        "Fundación Océanos Limpios", "Save The Forests", "Educa Sin Fronteras", 
        "Banco de Alimentos Central", "Refugio Animal Esperanza", "Tech For Good", 
        "Agua Viva ONG", "Sonrisas y Salud", "Raíces Verdes", "Mujeres en Acción"
    };

    private final String[] gnoEmails = {
        "contacto@oceanoslimpios.org",       // 1. Fundación Océanos Limpios
        "info@savetheforests.org",           // 2. Save The Forests
        "hola@educasinfronteras.org",        // 3. Educa Sin Fronteras
        "admin@bancoalimentoscentral.org",   // 4. Banco de Alimentos Central
        "adopciones@refugioesperanza.org",   // 5. Refugio Animal Esperanza
        "hello@techforgood.org",             // 6. Tech For Good
        "coordinacion@aguaviva.org",         // 7. Agua Viva ONG
        "info@sonrisasysalud.org",           // 8. Sonrisas y Salud
        "proyectos@raicesverdes.org",        // 9. Raíces Verdes
        "direccion@mujeresenaccion.org"      // 10. Mujeres en Acción
    };

    private final String[] firstNames = {"Ana", "Carlos", "Laura", "David", "Elena", "Jorge", "Marta", "Luis", "Sara", "Pablo", "Javier", "Carmen"};
    private final String[] lastNames = {"García", "Martínez", "López", "Sánchez", "Pérez", "Gómez", "Martín", "Ruiz", "Díaz", "Fernández", "Vidal", "Serra"};
    private final String[] departments = {"RRHH", "IT", "Marketing", "Finanzas", "Operaciones", "Ventas", "Legal"};

    private final Random random = new Random();

    private void seedGnos() {
        String defaultPassword = encoder.encode("Password123!");

        for (int i = 0; i < 10; i++) {
            User gnoUser = new User();
            gnoUser.setEmail(gnoEmails[i]); 
            gnoUser.setPasswordHash(defaultPassword);
            gnoUser.setRole(Role.ONG);
            userRepository.save(gnoUser);

            GnoProfile profile = new GnoProfile();
            profile.setUser(gnoUser);
            profile.setCif(String.format("G%08d", i + 1));
            profile.setOrganizationName(gnoNames[i]);
            profile.setContactName(firstNames[i] + " " + lastNames[i]);
            profile.setContactPhone("60010020" + i);
            profile.setContactEmail(gnoEmails[i]); 
            profile.setAddress("Avenida Diagonal " + (400 + i) + ", Barcelona");
            profile.setWebsite("https://www." + gnoNames[i].toLowerCase().replace(" ", "") + ".org");
            
            gnoProfileRepository.save(profile);
        }
    }

    private void seedEmployees() {
        String defaultPassword = encoder.encode("Password123!");

        User adminUser = new User();
        adminUser.setEmail("admin@verisure.com");
        adminUser.setPasswordHash(defaultPassword);
        adminUser.setRole(Role.ADMIN);
        userRepository.save(adminUser);

        EmployeeProfile adminProfile = new EmployeeProfile();
        adminProfile.setUser(adminUser);
        adminProfile.setEmployeeId(10000L); 
        adminProfile.setFirstName("Super");
        adminProfile.setLastName("Admin");
        adminProfile.setDepartment("Dirección");
        employeeProfileRepository.save(adminProfile);

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
            profile.setEmployeeId(20000L + i);
            profile.setFirstName(randomFirstName);
            profile.setLastName(randomLastName);
            profile.setDepartment(randomDept);
            
            employeeProfileRepository.save(profile);
        }
    }

    private void seedSdgs() {
        if (sdgRepository.count() == 0) {
            List<Sdg> sdgs = List.of(
                createSdg(1, "Fin de la pobreza"), createSdg(2, "Hambre cero"),
                createSdg(3, "Salud y bienestar"), createSdg(5, "Igualdad de género"),
                createSdg(6, "Agua limpia y saneamiento"), createSdg(7, "Energía asequible"),
                createSdg(9, "Industria e innovación"), createSdg(10, "Reducción desigualdades"),
                createSdg(12, "Consumo responsable"), createSdg(14, "Vida submarina")
            );
            sdgRepository.saveAll(sdgs);
        }
    }

    private Sdg createSdg(Integer id, String name) {
        Sdg sdg = new Sdg();
        sdg.setId(id);
        sdg.setName(name);
        return sdg;
    }

    private record ProjectTemplate(String title, String description, String impactUnit, String imageUrl) {}

    private void seedProjects() {

        List<GnoProfile> allGnos = gnoProfileRepository.findAll();
        List<Sdg> allSdgs = sdgRepository.findAll();
        OffsetDateTime now = OffsetDateTime.now();

        List<ProjectTemplate> templates = List.of(
            new ProjectTemplate("Reforestación en la Sierra de Collserola", "Únete a nuestro equipo para plantar árboles autóctonos y ayudar a recuperar el pulmón verde de la ciudad. Proporcionaremos todas las herramientas necesarias, guantes y un pequeño almuerzo a media mañana. Ideal para amantes de la naturaleza que quieran dejar una huella positiva en el medio ambiente.", "Árboles plantados", "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Gran Recogida del Banco de Alimentos", "Necesitamos voluntarios dinámicos para clasificar, organizar y empaquetar los alimentos donados en nuestra nave principal. Tu ayuda es vital para garantizar que miles de familias vulnerables puedan tener un plato de comida asegurado durante esta temporada. El trabajo es físico pero sumamente gratificante.", "Kilos de comida", "https://images.unsplash.com/photo-1594708767771-a7502209ff51?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Limpieza intensiva de la Playa de la Barceloneta", "Participa en nuestra jornada de limpieza costera para proteger la fauna marina. Pasaremos la mañana recogiendo plásticos, colillas y residuos de la arena y las rocas. Terminaremos la jornada con un breve taller de concienciación sobre el impacto de los microplásticos en los océanos.", "Kilos de plástico", "https://images.unsplash.com/photo-1618477461853-cf6ed80fbfc5?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Mentoring y Apoyo Escolar para Jóvenes", "Buscamos profesionales dispuestos a dedicar unas horas a ayudar a jóvenes en riesgo de exclusión social con sus tareas escolares. Especialmente buscamos apoyo en matemáticas, ciencias e inglés. Serás un modelo a seguir y ayudarás a combatir el abandono escolar temprano.", "Estudiantes apoyados", "https://images.unsplash.com/photo-1577896851231-70ef18881754?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Rescate y Cuidado en Refugio Animal", "Nuestro refugio está al máximo de su capacidad. Ven a ayudarnos a pasear a los perros, limpiar las instalaciones, socializar con los gatos y darles el cariño que necesitan mientras esperan ser adoptados. Una oportunidad perfecta para desconectar del estrés corporativo.", "Animales atendidos", "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Alfabetización Digital para la Tercera Edad", "Ayuda a reducir la brecha digital enseñando a personas mayores a usar un smartphone, hacer videollamadas con sus familiares, pedir citas médicas online o identificar fraudes por internet. Un proyecto de gran impacto humano que requiere paciencia y empatía.", "Personas formadas", "https://images.unsplash.com/photo-1573164713988-8665fc963095?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Construcción de Huertos Urbanos Sostenibles", "Transformaremos un espacio abandonado del barrio en un huerto comunitario sostenible. Aprenderemos técnicas de agricultura ecológica, instalaremos sistemas de riego por goteo y prepararemos los semilleros de temporada. ¡Ven con ropa que se pueda manchar!", "Metros cuadrados recuperados", "https://images.unsplash.com/photo-1464226184884-fa280b87c399?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Campaña de Sensibilización y Donación de Sangre", "Colabora con nuestro equipo médico en la campaña de donación. Tus funciones incluirán la recepción de donantes, entrega de refrigerios post-donación y difusión de información en los alrededores. Tu tiempo literalmente ayuda a salvar vidas.", "Donantes atendidos", "https://images.unsplash.com/photo-1615461066159-fea0960485d5?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Acompañamiento Telefónico contra la Soledad", "Proyecto 100% online/remoto. Dedica un par de horas a conversar por teléfono o videollamada con personas mayores que viven solas. Un gesto tan sencillo como escuchar y charlar sobre su día puede tener un impacto inmenso en su bienestar emocional.", "Horas de acompañamiento", "https://images.unsplash.com/photo-1516383740770-fbcc5ccbece0?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Recogida y Clasificación de Ropa de Abrigo", "Con la llegada del invierno, organizamos una campaña masiva de recogida de ropa térmica, mantas y calzado. Necesitamos manos para clasificar las donaciones por talla y género, y preparar los kits que serán distribuidos entre personas sin hogar.", "Kits preparados", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&w=1000&q=80")
        );

        String[] addresses = {"Gran Vía de les Corts Catalanes 585", "Paseo de la Castellana 15", "Avinguda del Paral·lel 71", "Calle de Alcalá 120", "Carrer de Balmes 22", "Avenida de las Ciencias s/n"};
        String[] cities = {"Barcelona", "Madrid", "Valencia", "Sevilla", "Bilbao"};
        
        LocationType[] locationTypes = LocationType.values(); 

        int projectCounter = 0;

        for (GnoProfile gno : allGnos) {

            List<ProjectTemplate> shuffledTemplates = new ArrayList<>(templates);
            Collections.shuffle(shuffledTemplates, random);

            for (int i = 0; i < 5; i++) {
                ProjectTemplate template = shuffledTemplates.get(i);
                Project project = new Project();
                project.setGno(gno);
                
                project.setTitle(template.title());
                project.setDescription(template.description());
                project.setRequiredVolunteers(random.nextInt(15) + 5);
                project.setImpactUnit(template.impactUnit());
                project.setImageUrl(template.imageUrl());
                project.setTotalHours(random.nextInt(6) + 2);

                LocationType randomLoc = locationTypes[random.nextInt(locationTypes.length)];
                project.setLocationType(randomLoc);

                if (randomLoc == LocationType.ONLINE) {
                    project.setAddress("https://meet.google.com/solidaridad-" + random.nextInt(9999));
                    project.setCity(null); 
                } else {
                    project.setAddress(addresses[random.nextInt(addresses.length)]);
                    project.setCity(cities[random.nextInt(cities.length)]);
                }

                int numSdgs = random.nextInt(3) + 1;
                for (int j = 0; j < numSdgs; j++) {
                    Sdg randomSdg = allSdgs.get(random.nextInt(allSdgs.size()));
                    if (!project.getSdgs().contains(randomSdg)) {
                        project.getSdgs().add(randomSdg);
                    }
                }

                if (i == 0) {
                    project.setStartDate(now.minusYears(3).minusMonths(2));
                    project.setEndDate(now.minusYears(3));
                    project.setStatus(StatusProject.COMPLETED);
                } else if (i == 1) {
                    project.setStartDate(now.minusYears(1).minusMonths(1));
                    project.setEndDate(now.minusYears(1));
                    project.setStatus(StatusProject.COMPLETED);
                } else if (i == 2) {
                    project.setStartDate(now.minusMonths(4));
                    project.setEndDate(now.minusMonths(3));
                    project.setStatus(StatusProject.CANCELED);
                } else if (i == 3) {
                    project.setStartDate(now.plusDays(15));
                    project.setEndDate(now.plusMonths(1));
                    project.setStatus(StatusProject.PUBLISHED);
                } else {
                    project.setStartDate(now.plusMonths(2));
                    project.setEndDate(now.plusMonths(3));
                    project.setStatus(StatusProject.PENDING);
                }

                projectRepository.save(project);
                projectCounter++;
            }
        }
    }

    private void seedApplicationsAndRecords() {

        List<Project> allProjects = projectRepository.findAll();
        List<EmployeeProfile> allEmployees = employeeProfileRepository.findAll();

        for (Project project : allProjects) {
            int applicantsCount = project.getRequiredVolunteers() + random.nextInt(8) - 2;
            if (applicantsCount < 2) applicantsCount = 2;
            if (applicantsCount > allEmployees.size()) applicantsCount = allEmployees.size();

            List<EmployeeProfile> shuffledEmployees = new ArrayList<>(allEmployees);
            Collections.shuffle(shuffledEmployees, random);

            for (int i = 0; i < applicantsCount; i++) {
                EmployeeProfile employee = shuffledEmployees.get(i);
                Application app = new Application();
                app.setProject(project);
                app.setEmployee(employee);

                boolean shouldCreateRecord = false;

                if (project.getStatus() == StatusProject.COMPLETED) {
                    if (i < project.getRequiredVolunteers()) {
                        app.setStatus(StatusApplication.CLOSED);
                        shouldCreateRecord = true; 
                    } else {
                        app.setStatus(StatusApplication.REJECTED);
                    }
                } else if (project.getStatus() == StatusProject.PUBLISHED) {
                    if (i < project.getRequiredVolunteers()) {
                        app.setStatus(StatusApplication.APPROVED);
                    } else {
                        app.setStatus(StatusApplication.WAITLISTED);
                    }
                } else if (project.getStatus() == StatusProject.CANCELED) {
                    app.setStatus(StatusApplication.CANCELED);
                } else {
                    app.setStatus(StatusApplication.PENDING);
                }

                Application savedApp = applicationRepository.save(app);

                if (shouldCreateRecord) {
                    ParticipationRecord record = new ParticipationRecord();
                    record.setApplication(savedApp);
                    record.setLoggedHours(BigDecimal.valueOf(project.getTotalHours()));
                    record.setImpactMetric(BigDecimal.ZERO);
                    
                    participationRecordRepository.save(record);
                }
            }
        }
    }
}

// Vamos a ir ampliando el seeder segun se vayan creando las entidades y los servicios.