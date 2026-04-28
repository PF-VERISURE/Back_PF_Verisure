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
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
        seedFavorites();
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

    // private void seedSdgs() {
    //     if (sdgRepository.count() == 0) {
    //         List<Sdg> sdgs = List.of(
    //             createSdg(1, "Fin de la pobreza"), createSdg(2, "Hambre cero"),
    //             createSdg(3, "Salud y bienestar"), createSdg(5, "Igualdad de género"),
    //             createSdg(6, "Agua limpia y saneamiento"), createSdg(7, "Energía asequible"),
    //             createSdg(9, "Industria e innovación"), createSdg(10, "Reducción desigualdades"),
    //             createSdg(12, "Consumo responsable"), createSdg(14, "Vida submarina")
    //         );
    //         sdgRepository.saveAll(sdgs);
    //     }
    // }

    private void seedSdgs() {
        if (sdgRepository.count() == 0) {
            List<Sdg> sdgs = List.of(
                createSdg(1, "Fin de la pobreza"), 
                createSdg(2, "Hambre cero"),
                createSdg(3, "Salud y bienestar"), 
                createSdg(5, "Igualdad de género"),
                createSdg(6, "Agua limpia y saneamiento"), 
                createSdg(7, "Energía asequible y no contaminante"),
                createSdg(9, "Ciudades y comunidades sostenibles"),  
                createSdg(10, "Reducción de las desigualdades"),     
                createSdg(12, "Producción y consumo responsables"),
                createSdg(14, "Vida submarina")
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

    private record ProjectTemplate(String title, String description, String impactUnit, String imageUrl, List<Integer> sdgIds) {}

    private void seedProjects() {

        List<GnoProfile> allGnos = gnoProfileRepository.findAll();
        List<Sdg> allSdgs = sdgRepository.findAll();
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        List<ProjectTemplate> templates = List.of(
        new ProjectTemplate("Reforestación en la Sierra de Collserola",  "Únete a nuestro equipo para plantar árboles autóctonos y ayudar a recuperar el pulmón verde de la ciudad. Proporcionaremos todas las herramientas necesarias, guantes y un pequeño almuerzo a media mañana. Ideal para amantes de la naturaleza que quieran dejar una huella positiva en el medio ambiente.", "Árboles plantados", "https://images.unsplash.com/photo-1542601906990-b4d3fb778b09?w=800&q=80", List.of(12, 3)),
        new ProjectTemplate("Gran Recogida del Banco de Alimentos", "Necesitamos voluntarios dinámicos para clasificar y empaquetar alimentos no perecederos. Tu ayuda es fundamental para garantizar que las familias más vulnerables de nuestra comunidad tengan acceso a alimentos nutritivos durante todo el año. No se requiere experiencia previa, solo energía y ganas de colaborar.", "Kilos de comida", "https://images.unsplash.com/photo-1594708767771-a7502209ff51?w=800&q=80", List.of(2, 1, 10)),
        new ProjectTemplate("Limpieza intensiva de la Playa de la Barceloneta", "Participa en nuestra jornada de limpieza costera. Juntos podemos mantener nuestras playas limpias y seguras para todos los visitantes. No olvides traer protector solar y una botella de agua reutilizable para mantenerte hidratado durante la actividad.", "Kilos de plástico", "https://images.unsplash.com/photo-1554265311-8799d909c767?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", List.of(14, 12)),
        new ProjectTemplate("Mentoring y Apoyo Escolar para Jóvenes", "Buscamos profesionales dispuestos a dedicar unas horas a la semana para guiar y apoyar a estudiantes de secundaria en sus estudios y desarrollo personal. Tu experiencia y conocimientos pueden marcar la diferencia en el futuro de un joven. ¡Anímate a participar!", "Estudiantes apoyados", "https://images.unsplash.com/photo-1577896851231-70ef18881754?w=800&q=80", List.of(10, 1)),
        new ProjectTemplate("Rescate y Cuidado en Refugio Animal", "Nuestro refugio está al máximo de su capacidad y necesitamos ayuda urgente para cuidar de los animales. Las tareas incluyen alimentar, limpiar jaulas, pasear perros y ofrecer cariño a los residentes. Cada pequeña acción cuenta para mejorar su calidad de vida mientras esperan un hogar definitivo.", "Animales atendidos", "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=800&q=80", List.of(12)),
        new ProjectTemplate("Alfabetización Digital para la Tercera Edad", "Ayuda a reducir la brecha digital enseñando habilidades tecnológicas básicas a personas mayores. Cubriremos temas como el uso del smartphone, correo electrónico, videollamadas y navegación segura por internet. No se requiere ser un experto en tecnología, solo paciencia y ganas de compartir conocimiento.", "Personas formadas", "https://images.unsplash.com/photo-1573164713988-8665fc963095?w=800&q=80", List.of(10, 9)),
        new ProjectTemplate("Construcción de Huertos Urbanos Sostenibles", "Transformaremos un espacio abandonado del barrio en un huerto comunitario productivo. Los voluntarios participarán en la preparación del terreno, construcción de bancales elevados, plantación de hortalizas y sistemas de riego eficiente. Una oportunidad perfecta para aprender sobre agricultura urbana y sostenibilidad.", "Metros cuadrados recuperados", "https://images.unsplash.com/photo-1464226184884-fa280b87c399?w=800&q=80", List.of(2, 12, 3)),
        new ProjectTemplate("Campaña de Sensibilización y Donación de Sangre", "Colabora con nuestro equipo médico en la campaña de donación de sangre. Las tareas incluyen informar a los donantes, gestionar el registro, ofrecer apoyo durante el proceso y asegurar el bienestar de los participantes. Tu contribución puede salvar vidas y fortalecer nuestra comunidad.", "Donantes atendidos", "https://images.unsplash.com/photo-1615461066159-fea0960485d5?w=800&q=80", List.of(3)),
        new ProjectTemplate("Acompañamiento Telefónico contra la Soledad", "Proyecto 100% online/remoto. Dedica un par de horas a la semana para charlar con personas mayores que viven solas. Tu compañía puede marcar una gran diferencia en su día a día. No se requiere experiencia previa, solo empatía y disponibilidad.", "Horas de acompañamiento", "https://images.unsplash.com/photo-1516383740770-fbcc5ccbece0?w=800&q=80", List.of(3, 10)),
        new ProjectTemplate("Recogida y Clasificación de Ropa de Abrigo", "Con la llegada del invierno, organizamos una campaña de recogida de ropa de abrigo para personas en situación de vulnerabilidad. Necesitamos voluntarios para clasificar, doblar y empaquetar las donaciones. Tu ayuda será fundamental para que muchas personas pasen un invierno más cálido.", "Kits preparados", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=800&q=80", List.of(1, 10)),
        new ProjectTemplate("Reparación de Equipos Informáticos para Escuelas", "Buscamos perfiles técnicos para revisar, reparar y poner a punto ordenadores y tablets que serán donados a escuelas con recursos limitados. Tu habilidad para arreglar tecnología puede abrir nuevas oportunidades educativas para muchos niños y jóvenes.", "Equipos restaurados", "https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=800&q=80", List.of(9, 12, 10)),
        new ProjectTemplate("Taller de Empleabilidad para Personas Refugiadas", "Acompaña a personas recién llegadas en su proceso de inserción laboral. Ofrece orientación sobre el mercado de trabajo local, revisa sus currículums y prepáralos para entrevistas. Tu apoyo puede ser clave para que encuentren una oportunidad de empleo y construyan un futuro en nuestro país.", "Personas asesoradas", "https://images.unsplash.com/photo-1552664730-d307ca884978?w=800&q=80", List.of(1, 10)),
        new ProjectTemplate("Rescate de Excedentes Agrícolas en el Maresme", "Únete a nuestra cuadrilla para recolectar frutas y verduras que de otro modo se perderían. Trabajaremos directamente con agricultores locales que donan sus excedentes. Una oportunidad perfecta para disfrutar del aire libre, aprender sobre agricultura y contribuir a la lucha contra el desperdicio alimentario.", "Kilos rescatados", "https://images.unsplash.com/photo-1629398781739-9caec1eb9409?q=80&w=3632&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", List.of(2, 12)),
        new ProjectTemplate("Renovación y Pintura en Planta de Pediatría", "Necesitamos personas creativas y con energía para dar nueva vida a la planta de pediatría del hospital. Las tareas incluyen pintar murales alegres, renovar mobiliario y crear un ambiente más acogedor para los niños hospitalizados. Tu arte y dedicación pueden hacer que su estancia sea más llevadera.", "Metros cuadrados pintados", "https://images.unsplash.com/photo-1583468982228-19f19164aee2?w=800&q=80", List.of(3)),
        new ProjectTemplate("Traducción Legal Pro Bono para Inmigrantes", "Iniciativa 100% remota para personas bilingües que quieran ayudar a personas inmigrantes con la traducción de documentos legales. No se requiere experiencia previa, solo empatía y ganas de compartir conocimiento.", "Documentos traducidos", "https://images.unsplash.com/photo-1451226428352-cf66bf8a0317?w=800&q=80", List.of(10)),
        new ProjectTemplate("Mantenimiento y Señalización de Senderos", "Jornada intensiva en la montaña para limpiar y señalizar senderos. Tu ayuda será fundamental para que los excursionistas puedan disfrutar de la naturaleza de forma segura y responsable.", "Kilómetros acondicionados", "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=800&q=80", List.of(12, 3)),
        new ProjectTemplate("Apoyo en Cocina y Servicio en Comedor Social", "Colabora con el equipo de cocina preparando menús, sirviendo comidas y manteniendo la limpieza del espacio. Tu apoyo es fundamental para garantizar que cada persona reciba una comida caliente y nutritiva en un ambiente acogedor.", "Comidas servidas", "https://images.unsplash.com/photo-1628717341663-0007b0ee2597?q=80&w=2071&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", List.of(2, 1)),
        new ProjectTemplate("Confección de Sacos de Dormir de Emergencia", "Taller grupal donde utilizaremos restos de telas para confeccionar sacos de dormir para personas en situación de calle. No se requiere experiencia previa, solo ganas de ayudar y un poco de habilidad con la costura.", "Sacos confeccionados", "https://images.unsplash.com/photo-1528698827591-e19ccd7bc23d?w=800&q=80", List.of(1, 12)),
        new ProjectTemplate("Censo y Observación de Aves Migratorias", "Acompaña a nuestros biólogos en la jornada de censo y observación de aves migratorias. Tu ayuda será fundamental para que podamos proteger a estas especies y su hábitat natural.", "Aves censadas", "https://images.unsplash.com/photo-1555169062-013468b47731?w=800&q=80", List.of(12, 14)),
        new ProjectTemplate("Hackathon Solidario para ONGs Locales", "Evento de fin de semana dirigido a desarrolladores y diseñadores para crear soluciones tecnológicas para ONGs locales. Se proporcionará alojamiento, comida y transporte en un entorno natural.", "Plataformas entregadas", "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=800&q=80", List.of(9)),
        new ProjectTemplate("Dinámicas de Teatro para Diversidad Funcional", "Participa como voluntario de apoyo en nuestro taller de teatro para personas con diversidad funcional. No se requiere experiencia previa, solo ganas de ayudar y un poco de habilidad con la improvisación.", "Sesiones realizadas", "https://images.unsplash.com/photo-1503095396549-807759245b35?w=800&q=80", List.of(10, 3)),
        new ProjectTemplate("Campaña de Restauración de Juguetes", "Durante las semanas previas a la Navidad, necesitamos personas con habilidades manuales para reparar y restaurar juguetes donados. Tu ayuda permitirá que muchos niños reciban un regalo en estas fechas tan especiales.", "Juguetes preparados", "https://images.unsplash.com/photo-1748534515262-a80df6e1d400?q=80&w=1938&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", List.of(12, 10)),
        new ProjectTemplate("Asesoría Legal para Prevención de Desahucios", "Buscamos profesionales del derecho dispuestos a ofrecer asesoramiento legal gratuito a familias en riesgo de desahucio. Tu apoyo puede marcar la diferencia para que muchas personas puedan mantener su hogar.", "Familias asesoradas", "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?w=800&q=80", List.of(1, 10)),
        new ProjectTemplate("Sesiones de Mindfulness para Supervivientes", "Si eres instructor/a certificado, dona una hora de tu tiempo para impartir sesiones de mindfulness a personas que han superado situaciones de violencia. Tu guía puede ayudarles a encontrar la calma y el equilibrio en su proceso de recuperación.", "Sesiones impartidas", "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800&q=80", List.of(3, 5)),
        new ProjectTemplate("Taller de Carpintería: Cajas Nido para Aves", "Aprenderemos a ensamblar cajas nido y refugios para aves en entornos urbanos. No se requiere experiencia previa, solo ganas de aprender y un poco de habilidad con las herramientas.", "Cajas construidas", "https://images.unsplash.com/photo-1534237710431-e2fc698436d0?w=800&q=80", List.of(12)),
        new ProjectTemplate("Acompañamiento Musical en Residencias", "¿Tocas algún instrumento o cantas? Ven a compartir tu talento con los residentes de la tercera edad. Una tarde de música puede alegrarles el día y hacerles sentir acompañados.", "Horas de concierto", "https://images.unsplash.com/photo-1637071220527-fbb98fa15892?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", List.of(3, 10)), 
        new ProjectTemplate("Limpieza y Recuperación del Cauce del Río", "Jornada medioambiental enfocada en la retirada de residuos del cauce del río y sus alrededores. Tu ayuda será fundamental para proteger el ecosistema y mejorar el entorno natural.", "Kilos de residuos", "https://images.unsplash.com/photo-1675725594644-ed8d00bc6887?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", List.of(6, 14, 12)),
        new ProjectTemplate("Torneo de Fútbol Inclusivo", "Necesitamos árbitros, encargados de marcador y apoyo logístico para que el torneo sea un éxito. No se requiere experiencia previa, solo ganas de ayudar y un poco de habilidad con el deporte.", "Partidos arbitrados", "https://images.unsplash.com/photo-1570498839593-e565b39455fc?q=80&w=1035&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", List.of(3, 10)),
        new ProjectTemplate("Educación Financiera para Familias", "Imparte talleres prácticos sobre cómo interpretar el recibo de la luz, el gas o el agua, cómo comparar tarifas y cómo ahorrar en la factura mensual. Tu conocimiento puede ayudar a muchas familias a mejorar su economía doméstica.", "Familias formadas", "https://images.unsplash.com/photo-1554224155-6726b3ff858f?w=800&q=80", List.of(1, 10)),
        new ProjectTemplate("Muralismo Comunitario para la Integración", "Transformaremos un muro gris y degradado en una obra de arte que refleje la diversidad y la riqueza cultural de nuestro barrio. No se requiere experiencia previa, solo ganas de pintar y un poco de creatividad.", "Metros de mural", "https://images.unsplash.com/photo-1574674826492-cbcd09c5db3c?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", List.of(10, 9))
    );

        String[] addresses = {"Gran Vía de les Corts Catalanes 585", "Paseo de la Castellana 15", "Avinguda del Paral·lel 71", "Calle de Alcalá 120", "Carrer de Balmes 22", "Avenida de las Ciencias s/n","Calle de la Paz 123","Avenida de la Constitución 456","Calle de la Libertad 789","Avenida de la Independencia 101", "Calle del Sol 123", "Calle de la Luna 456", "Calle del Mar 789", "Calle del Cielo 101", "Calle de la Montaña 123", "Calle de la Playa 456", "Calle del Río 789", "Calle del Lago 101", "Calle del Bosque 123", "Calle del Campo 456", "Calle del Monte 789", "Calle de la Colina 101"};
        String[] cities = {"Barcelona", "Madrid", "Valencia", "Sevilla", "Bilbao", "Zaragoza","Palma de Mallorca","Malaga"};
        LocationType[] locationTypes = LocationType.values(); 

        List<Integer> timelineYearsAgo = new ArrayList<>();
        for(int i=0; i<4; i++) timelineYearsAgo.add(5);
        for(int i=0; i<8; i++) timelineYearsAgo.add(4);
        for(int i=0; i<12; i++) timelineYearsAgo.add(3);
        for(int i=0; i<16; i++) timelineYearsAgo.add(2);
        for(int i=0; i<20; i++) timelineYearsAgo.add(1);
        for(int i=0; i<20; i++) timelineYearsAgo.add(0);

        List<ProjectTemplate> shuffledTemplates = new ArrayList<>(templates);
        Collections.shuffle(shuffledTemplates, random);

        int yearZeroCounter = 0;

        for (int i = 0; i < timelineYearsAgo.size(); i++) {
            int yearsAgo = timelineYearsAgo.get(i);
        
            int availableGnosPool = 10 - (yearsAgo * 2); 
            if (availableGnosPool < 2) availableGnosPool = 2;
            GnoProfile gno = allGnos.get(random.nextInt(availableGnosPool));

            ProjectTemplate template = shuffledTemplates.get(i % shuffledTemplates.size());
            Project project = new Project();
            project.setGno(gno);
            project.setTitle(template.title());
            project.setDescription(template.description());
            project.setRequiredVolunteers(random.nextInt(15) + 5);
            project.setImpactUnit(template.impactUnit());
            project.setImageUrl(template.imageUrl());
            project.setTotalHours(random.nextInt(8) + 2);

            LocationType randomLoc = locationTypes[random.nextInt(locationTypes.length)];
            project.setLocationType(randomLoc);
            if (randomLoc == LocationType.ONLINE) {
                project.setAddress("https://meet.google.com/sala-" + random.nextInt(9999));
                project.setCity(null);
            } else {
                project.setAddress(addresses[random.nextInt(addresses.length)]);
                project.setCity(cities[random.nextInt(cities.length)]);
            }

            int numSdgs = random.nextInt(3) + 1;
            for (Integer sdgId : template.sdgIds()) {
                allSdgs.stream()
                       .filter(s -> s.getId().equals(sdgId))
                       .findFirst()
                       .ifPresent(sdg -> project.getSdgs().add(sdg));
            }

            if (yearsAgo > 0) {
                int randomMonth = random.nextInt(11) + 1;
                project.setStartDate(now.minusYears(yearsAgo).withMonth(randomMonth).withDayOfMonth(random.nextInt(20) + 1));
                project.setEndDate(project.getStartDate().plusDays(random.nextInt(5) + 1));
                project.setStatus(random.nextInt(10) > 8 ? StatusProject.CANCELED : StatusProject.COMPLETED);

            } else {

                if (yearZeroCounter < 5) {
                    project.setStartDate(now.minusMonths(random.nextInt(3) + 1));
                    project.setEndDate(project.getStartDate().plusDays(3));
                    project.setStatus(StatusProject.COMPLETED);
                } else if (yearZeroCounter < 10) {
                    project.setStartDate(now.minusDays(random.nextInt(5) + 1));
                    project.setEndDate(now.plusDays(15)); 
                    project.setStatus(StatusProject.PUBLISHED);
                } else if (yearZeroCounter < 15) {
                    project.setStartDate(now.plusMonths(1));
                    project.setEndDate(now.plusMonths(1).plusDays(3));
                    project.setStatus(StatusProject.PUBLISHED);
                } else {
                    project.setStartDate(now.plusMonths(2));
                    project.setEndDate(now.plusMonths(2).plusDays(5));
                    project.setStatus(StatusProject.PENDING);
                }
                yearZeroCounter++;
            }

            project.setCreatedAt(project.getStartDate().minusDays(30));

            projectRepository.save(project);
        }
    }

    private void seedApplicationsAndRecords() {
        List<Project> allProjects = projectRepository.findAll();
        List<EmployeeProfile> allEmployees = employeeProfileRepository.findAll();

        for (Project project : allProjects) {
            
            if (project.getStatus() == StatusProject.PENDING) {
                continue;
            }

            int applicantsCount = project.getRequiredVolunteers() + random.nextInt(8) - 1;
            if (applicantsCount < 2) applicantsCount = 2;
            if (applicantsCount > allEmployees.size()) applicantsCount = allEmployees.size();

            List<EmployeeProfile> shuffledEmployees = new ArrayList<>(allEmployees);
            Collections.shuffle(shuffledEmployees, random);

            for (int i = 0; i < applicantsCount; i++) {
                EmployeeProfile employee = shuffledEmployees.get(i);
                Application app = new Application();
                app.setProject(project);
                app.setEmployee(employee);

                int applyDelay = random.nextInt(28) + 1;
                app.setCreatedAt(project.getCreatedAt().plusDays(applyDelay));

                boolean inQuota = i < project.getRequiredVolunteers();

                if (project.getStatus() == StatusProject.CANCELED) {
                    app.setStatus(StatusApplication.CANCELED);
                
                } else if (project.getStatus() == StatusProject.COMPLETED) {

                    if (inQuota) {
                        app.setStatus(StatusApplication.CLOSED);
                        
                        Application savedApp = applicationRepository.save(app);
                        
                        ParticipationRecord record = new ParticipationRecord();
                        record.setApplication(savedApp);
                        record.setLoggedHours(java.math.BigDecimal.valueOf(project.getTotalHours()));
                        record.setImpactMetric(java.math.BigDecimal.ZERO);
                        
                        record.setCreatedAt(project.getEndDate().plusDays(1));
                        
                        participationRecordRepository.save(record);
                        continue;

                    } else {
                        app.setStatus(StatusApplication.REJECTED);
                    }

                } else if (project.getStatus() == StatusProject.PUBLISHED) {
                    if (inQuota) {
                        app.setStatus(StatusApplication.APPROVED);
                    } else {
                        app.setStatus(StatusApplication.WAITLISTED);
                    }
                }

                applicationRepository.save(app);
            }
        }
    }

    private void seedFavorites() {
        List<Project> allProjects = projectRepository.findAll();
        List<EmployeeProfile> allEmployees = employeeProfileRepository.findAll();

        for (Project project : allProjects) {

            int likesCount = random.nextInt(20); 
            if (likesCount > allEmployees.size()) likesCount = allEmployees.size();

            List<EmployeeProfile> shuffledEmployees = new ArrayList<>(allEmployees);
            Collections.shuffle(shuffledEmployees, random);

            for (int i = 0; i < likesCount; i++) {
                User user = shuffledEmployees.get(i).getUser();
                
                com.verisure.backend.entity.UserFavorite favorite = new com.verisure.backend.entity.UserFavorite();
                favorite.setUser(user);
                favorite.setProject(project);

                int daysAfterCreation = random.nextInt(10) + 1;
                favorite.setCreatedAt(project.getCreatedAt().plusDays(daysAfterCreation));

                userFavoriteRepository.save(favorite);
            }
        }
    }
}

// Seeder creado para la demo con un historico de 5 años y con tendencia de evolucion incremental.