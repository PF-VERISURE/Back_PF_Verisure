package com.verisure.backend.service;

import com.verisure.backend.entity.User;
import com.verisure.backend.mapper.UserMapper;
import com.verisure.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el email: " + email)); 
    }
}