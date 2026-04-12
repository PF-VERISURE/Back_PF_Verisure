package com.verisure.backend;

import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.Role;
import com.verisure.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
	
    @Bean
    CommandLineRunner initData(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            // Solo creamos si la base está vacía para no duplicar en cada reinicio
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
                
                System.out.println("✅ Usuarios de prueba creados con éxito");
            }
        };
    }
}