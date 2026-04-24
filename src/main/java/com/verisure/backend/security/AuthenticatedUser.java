package com.verisure.backend.security;

public record AuthenticatedUser(
    
    Long userId, 
    String email

) {

}