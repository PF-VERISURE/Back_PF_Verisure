package com.verisure.backend.config;

import com.verisure.backend.entity.EmployeeProfile;
import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.Role;
import com.verisure.backend.repository.SdgRepository;
import com.verisure.backend.repository.UserRepository;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SdgRepository sdgRepository;
    private final BCryptPasswordEncoder encoder;

    public DataSeeder(UserRepository userRepository, SdgRepository sdgRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.sdgRepository = sdgRepository;
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
            String[] employeeNames = { "Laura", "Carlos", "Marta" };
            String[] employeeLastNames = { "Gómez", "Ruiz", "Sánchez" };
            String[] departments = { "IT Support", "Ventas", "Marketing" };
            Long[] empIds = { 100456L, 100457L, 100458L };

            for (int i = 0; i < 3; i++) {
                User employee = new User();
                employee.setEmail("empleado" + (i + 1) + "@verisure.com");
                // OJO AQUÍ: La contraseña es user123
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
    }

    private Sdg createSdg(Integer id, String name) {
        Sdg sdg = new Sdg();
        sdg.setId(id);
        sdg.setName(name);
        return sdg;
    }
}
// Vamos a ir ampliando el seeder segun se vayan creando las entidades y los
// servicios.