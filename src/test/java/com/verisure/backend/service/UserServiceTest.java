package com.verisure.backend.service;

import com.verisure.backend.entity.User;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Happy Path: Retornar entidad User cuando el email existe")
    void findByEmail_Success() {

        String email = "maria@verisure.com";

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.findByEmail(email);

        assertNotNull(result, "El usuario devuelto no debería ser nulo");
        assertEquals(email, result.getEmail(), "El email del usuario no coincide");
        //assertEquals("casigraduadas@femcodersP8.com", result.getEmail(), "El email del usuario no coincide");
        
        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Sad Path: Lanzar ResourceNotFoundException cuando el email no existe")
    void findByEmail_NotFound() {

        String email = "noexiste@verisure.com";
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findByEmail(email);
        }, "Debería lanzar ResourceNotFoundException para un email inexistente");

        // assertThrows(NullPointerException.class, () -> {
        //     userService.findByEmail(email);
        // }, "Debería lanzar NullPointerException para un email inexistente");

        verify(userRepository).findByEmail(email);
    }
}