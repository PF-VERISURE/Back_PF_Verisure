package com.verisure.backend.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.verisure.backend.dto.request.LoginRequestDTO;
import com.verisure.backend.dto.response.EmployeeLoginResponseDTO;
import com.verisure.backend.dto.response.ErrorResponseDTO;
import com.verisure.backend.dto.response.GnoLoginResponseDTO;
import com.verisure.backend.dto.response.UserAuthResponseDTO;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.Role;
import com.verisure.backend.repository.UserRepository;
import com.verisure.backend.security.CustomAuthenticationManager;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final CustomAuthenticationManager customAuthenticationManager;
    private final String jwtSecret;
    private final UserRepository userRepository;

    public JWTAuthenticationFilter(CustomAuthenticationManager customAuthenticationManager, String jwtSecret    , UserRepository userRepository) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.jwtSecret = jwtSecret;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDTO credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    credentials.email(), 
                    credentials.password()
            );
            return customAuthenticationManager.authenticate(authentication);
        } catch (AuthenticationException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer las credenciales del login", e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(401,"Usuario o contraseña incorrecta");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); 
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        response.getWriter().write(mapper.writeValueAsString(errorResponse));
        response.getWriter().flush();
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        
        List<String> roles = authResult.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .collect(Collectors.toList());

        String email = authResult.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        String token = JWT.create()
                .withSubject(authResult.getName()) 
                .withClaim("roles", roles)
                .withClaim("userId", user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + (12 * 60 * 60 * 1000))) 
                .sign(Algorithm.HMAC512(jwtSecret));

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");

        Object profileData = null;
        Role userRole = user.getRole();

        if (userRole == Role.ONG) {
            if (user.getGnoProfile() != null) {
                profileData = new GnoLoginResponseDTO(
                    user.getGnoProfile().getContactName(),
                    user.getGnoProfile().getOrganizationName()
                );
            }
        } else {
            if (user.getEmployeeProfile() != null) {
                profileData = new EmployeeLoginResponseDTO(
                    user.getEmployeeProfile().getFirstName(),
                    user.getEmployeeProfile().getDepartment()
                );
            }
        }

        UserAuthResponseDTO authResponse = new UserAuthResponseDTO(userRole.name(), profileData);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(authResponse));
        response.getWriter().flush();
    }
}