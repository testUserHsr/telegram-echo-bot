package ru.tech.telegrambot.security.service;

import org.springframework.security.authentication.BadCredentialsException;
import ru.tech.telegrambot.security.dto.RegistrationRequest;
import ru.tech.telegrambot.security.dto.AuthResponse;
import ru.tech.telegrambot.security.dto.LoginRequest;

/**
 * Service handling core security operations: user registration and authentication
 */
public interface SecurityService {
    /**
     * Registers a new user in the system.
     *
     * @param request contains user registration details
     */
    void register(RegistrationRequest request);

    /**
     * Authenticates user and generates authentication token.
     *
     * @param loginRequest contains user credentials
     * @return authentication response with JWT token
     * @throws BadCredentialsException if authentication fails
     */
    AuthResponse authenticateUser(LoginRequest loginRequest);
}
