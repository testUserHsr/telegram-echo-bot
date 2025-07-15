package ru.tech.telegrambot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tech.telegrambot.controller.swagger.AuthControllerApi;
import ru.tech.telegrambot.security.dto.AuthResponse;
import ru.tech.telegrambot.security.dto.LoginRequest;
import ru.tech.telegrambot.security.dto.RegistrationRequest;
import ru.tech.telegrambot.model.dto.util.SimpleResponse;
import ru.tech.telegrambot.security.service.SecurityService;

/**
 * Authentication Controller for new user registration.
 *
 * @see RegistrationRequest
 */
@RequiredArgsConstructor
@RestController
public class AuthController implements AuthControllerApi {
    private final SecurityService securityService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> register(
            @RequestBody @Valid RegistrationRequest registrationRequest) {
        securityService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new SimpleResponse("User created!"));
    }

    @Override
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authUser(
            @RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(securityService.authenticateUser(loginRequest));
    }
}
