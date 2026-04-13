package com.verisure.backend.config;

import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.Role;
import com.verisure.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public DataSeeder(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // 1. SEEDER DE USUARIOS
        if (userRepository.count() == 0) {

            // Admin
            User admin = new User();
            admin.setEmail("admin@verisure.com");
            admin.setPasswordHash(encoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);

            // Empleado
            User employee = new User();
            employee.setEmail("empleado@verisure.com");
            employee.setPasswordHash(encoder.encode("user123"));
            employee.setRole(Role.EMPLOYEE);
            userRepository.save(employee);

            // ONG
            User ong = new User();
            ong.setEmail("ong@verisure.com");
            ong.setPasswordHash(encoder.encode("ong123"));
            ong.setRole(Role.ONG);
            userRepository.save(ong);
            
            System.out.println("✅ Usuarios de prueba creados con éxito desde el DataSeeder");
        }

        // Vamos a ir ampliando el seeder segun se vayan creando las entidades y los servicios.

        // 2. SEEDER DE PERFILES (Para el futuro)
        /*
        if (employeeProfileRepository.count() == 0) {
            // Aquí crearemos los perfiles cuando tengamos las tablas listas
        }
        */
    }
}