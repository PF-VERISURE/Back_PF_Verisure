package com.verisure.backend.service;

import com.verisure.backend.entity.User;

public interface UserService {
    
    User findByEmail(String email);

}