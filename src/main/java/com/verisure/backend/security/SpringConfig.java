package com.verisure.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.verisure.backend.repository.UserRepository;
import com.verisure.backend.security.filter.JWTAuthenticationFilter;
import com.verisure.backend.security.filter.JWTAuthorizationFilter;

@Configuration
public class SpringConfig {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final String jwtSecret;
    private final UserRepository userRepository;

    public SpringConfig(CustomAuthenticationManager customAuthenticationManager,
            @Value("${jwt.secret}") String jwtSecret, UserRepository userRepository) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtSecret = jwtSecret;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(
            customAuthenticationManager,
            jwtSecret,
            userRepository
        );

        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/gnos/profile").hasRole("ONG")
                        .requestMatchers("/api/v1/gnos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employees/profile").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/sdgs/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/projects").hasRole("ONG")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/projects/{id}").hasRole("ONG")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/projects/{id}").hasRole("ONG")
                        .requestMatchers(HttpMethod.GET,"/api/v1/projects/my-projects").hasRole("ONG")
                        .requestMatchers(HttpMethod.GET,"/api/v1/projects/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/projects/pending").hasAnyRole("ADMIN", "ONG")
                        .requestMatchers(HttpMethod.GET, "/api/v1/applications/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/v1/projects/published").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/v1/applications/**").hasRole("EMPLOYEE")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(jwtSecret), JWTAuthenticationFilter.class)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}