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
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

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
            new ProjectTemplate("Recogida y Clasificación de Ropa de Abrigo", "Con la llegada del invierno, organizamos una campaña masiva de recogida de ropa térmica, mantas y calzado. Necesitamos manos para clasificar las donaciones por talla y género, y preparar los kits que serán distribuidos entre personas sin hogar.", "Kits preparados", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Reparación de Equipos Informáticos para Escuelas", "Buscamos perfiles técnicos para revisar, formatear y reparar ordenadores donados por empresas. Estos equipos serán destinados a escuelas públicas con escasez de recursos y a familias vulnerables para reducir la brecha digital. No es necesario ser un experto, solo tener conocimientos básicos y ganas de ayudar.", "Equipos restaurados", "https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Taller de Empleabilidad para Personas Refugiadas", "Acompaña a personas recién llegadas en su proceso de inserción sociolaboral. Las sesiones consistirán en ayudarles a redactar su currículum adaptado al mercado local y realizar simulacros de entrevistas de trabajo. Tu experiencia profesional puede ser la llave que les abra las puertas a una nueva vida.", "Personas asesoradas", "https://images.unsplash.com/photo-1552664730-d307ca884978?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Rescate de Excedentes Agrícolas en el Maresme", "Únete a nuestra cuadrilla para recolectar frutas y verduras que, por razones estéticas, no van a ser comercializadas pero están en perfecto estado. Todo lo recolectado se entregará directamente a comedores sociales esa misma tarde. Una forma excelente de combatir el desperdicio alimentario haciendo ejercicio al aire libre.", "Kilos rescatados", "https://images.unsplash.com/photo-1615486171448-4fd9b0051821?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Renovación y Pintura en Planta de Pediatría", "Necesitamos personas creativas y con energía para llenar de color los pasillos y salas de espera del área infantil del hospital. Crearemos murales temáticos amables que ayuden a reducir la ansiedad de los más pequeños durante sus ingresos. Proporcionamos toda la pintura, brochas y material de protección.", "Metros cuadrados pintados", "https://images.unsplash.com/photo-1583468982228-19f19164aee2?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Traducción Legal Pro Bono para Inmigrantes", "Iniciativa 100% remota para personas bilingües (Inglés, Francés o Árabe a Español). Ayudarás a traducir documentación oficial, expedientes de asilo e información de derechos básicos para personas migrantes. Un trabajo de escritorio que tiene un impacto directo y vital en la regularización de estas familias.", "Documentos traducidos", "https://images.unsplash.com/photo-1451226428352-cf66bf8a0317?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Mantenimiento y Señalización de Senderos Naturales", "Jornada intensiva en la montaña para limpiar de maleza los caminos principales y restaurar la señalización de madera deteriorada. Esta labor es fundamental para prevenir incendios forestales en verano y proteger la biodiversidad de la zona. Se requiere buena condición física y calzado de montaña adecuado.", "Kilómetros acondicionados", "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Apoyo en Cocina y Servicio en Comedor Social", "Colabora con el equipo de cocina preparando los ingredientes, sirviendo los platos calientes y ayudando en la recogida y limpieza del salón. Es un turno de alta intensidad donde se sirven más de 300 comidas en apenas dos horas. Vivirás de primera mano la importancia de la solidaridad de proximidad.", "Comidas servidas", "https://images.unsplash.com/photo-1591189863430-ab8a528ae44b?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Confección de Sacos de Dormir de Emergencia", "Taller grupal donde utilizaremos restos de telas de fábricas locales y aislantes térmicos para coser sacos de dormir de emergencia. Estos sacos serán repartidos durante las rutas nocturnas de atención a personas sin hogar. No es imprescindible saber coser a máquina, también necesitamos manos para cortar y medir.", "Sacos confeccionados", "https://images.unsplash.com/photo-1528698827591-e19ccd7bc23d?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Censo y Observación de Aves Migratorias", "Acompaña a nuestros biólogos en la jornada de observación de humedales para registrar las poblaciones de aves de paso. Los datos recogidos servirán para estudios de impacto del cambio climático en las rutas migratorias europeas. Trae tus propios prismáticos si tienes, ¡y mucha paciencia para estar en silencio!", "Aves censadas", "https://images.unsplash.com/photo-1555169062-013468b47731?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Hackathon Solidario para ONGs Locales", "Evento de fin de semana dirigido a desarrolladores, diseñadores y gestores de proyectos. Trabajaremos en equipos ágiles para crear o mejorar las páginas web y sistemas de gestión de pequeñas asociaciones de barrio que no pueden permitirse perfiles técnicos. Habrá pizza, café infinito y mucho código con propósito.", "Plataformas entregadas", "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Dinámicas de Teatro para Diversidad Funcional", "Participa como voluntario de apoyo en nuestro taller de expresión corporal y teatro inclusivo. Ayudarás a los monitores a guiar los ejercicios, fomentando la autoconfianza y las habilidades sociales de los participantes. Prepárate para reír, improvisar y perder la vergüenza en un ambiente seguro y familiar.", "Sesiones realizadas", "https://images.unsplash.com/photo-1503095396549-807759245b35?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Campaña de Restauración de Juguetes", "Durante las semanas previas a la Navidad, necesitamos ayuda para revisar, limpiar, poner pilas y empaquetar los juguetes donados. Nuestro objetivo es que cada paquete parezca nuevo y llegue en perfectas condiciones a familias con dificultades económicas. Un proyecto entrañable que devuelve la ilusión a los más pequeños.", "Juguetes preparados", "https://images.unsplash.com/photo-1560856218-0da41ac1c6ea?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Asesoría Legal para Prevención de Desahucios", "Buscamos profesionales del derecho dispuestos a ofrecer orientación inicial gratuita a familias que se enfrentan a procesos de ejecución hipotecaria o cortes de suministros. Les ayudaremos a entender los documentos judiciales y a solicitar la asistencia jurídica gratuita. Empatía y rigor legal al servicio de los más vulnerables.", "Familias asesoradas", "https://images.unsplash.com/photo-1589829085413-56de8ae18c73?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Sesiones de Mindfulness para Supervivientes", "Si eres instructor/a certificado, dona una hora de tu tiempo para guiar una sesión de relajación y yoga suave. El grupo está formado por mujeres que han superado situaciones de violencia de género y buscan reconectar con su cuerpo en un espacio seguro. Se valorará experiencia previa en metodologías sensibles al trauma.", "Sesiones impartidas", "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Taller de Carpintería: Cajas Nido para Aves", "Aprenderemos a ensamblar cajas nido y refugios para murciélagos utilizando madera certificada. Estos refugios se instalarán posteriormente en parques urbanos para fomentar el control biológico de plagas de mosquitos de forma natural. Una actividad manual, divertida y perfecta para ensuciarse las manos por una buena causa.", "Cajas construidas", "https://images.unsplash.com/photo-1534237710431-e2fc698436d0?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Acompañamiento Musical en Residencias", "¿Tocas algún instrumento o cantas? Ven a compartir tu talento musical durante la tarde del domingo con los residentes del centro geriátrico. La música en directo es una herramienta terapéutica increíble que despierta recuerdos, fomenta la interacción y rompe la monotonía institucional. ¡Se aceptan peticiones de boleros!", "Horas de concierto", "https://images.unsplash.com/photo-1444392061266-993ebfb12284?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Limpieza y Recuperación del Cauce del Río", "Jornada medioambiental enfocada en la retirada de residuos voluminosos y especies vegetales invasoras de las orillas del río. Es un trabajo duro y húmedo, pero vital para permitir que la flora y fauna autóctona recupere su espacio natural. Se proveerán botas de agua, guantes gruesos y herramientas de desbroce.", "Kilos de residuos", "https://images.unsplash.com/photo-1518558997970-4f114c5770c0?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Organización de Torneo de Fútbol Inclusivo", "Necesitamos árbitros, encargados de marcador, fotógrafos y animadores para nuestro torneo de fin de semana. Equipos formados por jóvenes de distintos orígenes socioeconómicos competirán en un ambiente de respeto y compañerismo. El deporte es la mejor excusa para romper barreras invisibles en nuestro distrito.", "Partidos arbitrados", "https://images.unsplash.com/photo-1518605368461-1e1e38ddf594?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Educación Financiera para Familias", "Imparte talleres prácticos sobre cómo interpretar facturas de la luz, crear un presupuesto familiar básico o entender los microcréditos para evitar el sobreendeudamiento. Estos conocimientos son fundamentales para que las personas en riesgo de exclusión tomen el control de su economía. Se requiere empatía y capacidad pedagógica.", "Familias formadas", "https://images.unsplash.com/photo-1554224155-6726b3ff858f?auto=format&fit=crop&w=1000&q=80"),
            new ProjectTemplate("Muralismo Comunitario para la Integración", "Transformaremos un muro gris y degradado del barrio en una obra de arte colectiva. No hace falta ser un artista experto; los vecinos y voluntarios pintaremos juntos un diseño pre-trazado que celebra la diversidad cultural de la zona. Trae ropa cómoda que no te importe manchar de pintura y ganas de conocer a la comunidad.", "Metros de mural", "https://images.unsplash.com/photo-1499803270242-467f70b77134?auto=format&fit=crop&w=1000&q=80")
            
        );

        String[] addresses = {"Gran Vía de les Corts Catalanes 585", "Paseo de la Castellana 15", "Avinguda del Paral·lel 71", "Calle de Alcalá 120", "Carrer de Balmes 22", "Avenida de las Ciencias s/n","Calle de la Paz 123","Avenida de la Constitución 456","Calle de la Libertad 789","Avenida de la Independencia 101"};
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
            for (int j = 0; j < numSdgs; j++) {
                Sdg randomSdg = allSdgs.get(random.nextInt(allSdgs.size()));
                if (!project.getSdgs().contains(randomSdg)) project.getSdgs().add(randomSdg);
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