package com.verisure.backend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.verisure.backend.security.filter.JWTAuthenticationFilter;
import com.verisure.backend.security.filter.JWTAuthorizationFilter;

@Configuration
public class SpringConfig {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final String jwtSecret;

    public SpringConfig(CustomAuthenticationManager customAuthenticationManager, @Value("${jwt.secret}") String jwtSecret) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtSecret = jwtSecret;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(customAuthenticationManager, jwtSecret);
        
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");

        http
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .authorizeHttpRequests(request -> request
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/employees/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .requestMatchers("/api/v1/ong/**").hasAnyRole("ADMIN", "ONG")
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated()
            )
            .addFilter(authenticationFilter)
            .addFilterAfter(new JWTAuthorizationFilter(jwtSecret), JWTAuthenticationFilter.class)
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}