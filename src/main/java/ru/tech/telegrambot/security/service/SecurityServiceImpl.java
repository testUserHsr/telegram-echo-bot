package ru.tech.telegrambot.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.tech.telegrambot.security.dto.RegistrationRequest;
import ru.tech.telegrambot.security.dto.AppUserDetails;
import ru.tech.telegrambot.security.jwt.JwtUtils;
import ru.tech.telegrambot.security.dto.AuthResponse;
import ru.tech.telegrambot.security.dto.LoginRequest;
import ru.tech.telegrambot.service.user.service.UserService;

/**
 * Service handling security-related operations: registration and JWT authentication.
 */
@RequiredArgsConstructor
@Service
public class SecurityServiceImpl implements SecurityService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @Override
    public void register(RegistrationRequest request) {
        userService.register(request);
    }

    /**
     * Authenticates user and generates JWT token.
     *
     * @param loginRequest contains username and password
     * @return AuthResponse with JWT token
     * @throws BadCredentialsException if authentication fails
     */
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticate(loginRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        return new AuthResponse(token);
    }

    /**
     * Performs Spring Security authentication.
     *
     * @throws BadCredentialsException if credentials are invalid
     */
    private Authentication authenticate(LoginRequest loginRequest) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
    }
}
