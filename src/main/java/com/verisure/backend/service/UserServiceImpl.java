package com.verisure.backend.service;

import com.verisure.backend.entity.User;
import com.verisure.backend.repository.UserRepository;
import com.verisure.backend.security.UserDetail;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new UserDetail(user))
                .orElseThrow(() -> new UsernameNotFoundException("Credenciales incorrectas: usuario" + email + " no encontrado."));
    }
}