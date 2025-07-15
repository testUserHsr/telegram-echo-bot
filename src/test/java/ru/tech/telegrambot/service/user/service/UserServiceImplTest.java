package ru.tech.telegrambot.service.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tech.telegrambot.exception.TokenGenerationException;
import ru.tech.telegrambot.exception.UserNotFoundException;
import ru.tech.telegrambot.exception.UserRegistrationException;
import ru.tech.telegrambot.exception.UsernameAlreadyExistsException;
import ru.tech.telegrambot.security.dto.RegistrationRequest;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.model.mapper.UserMapper;
import ru.tech.telegrambot.repository.AppUserRepository;
import ru.tech.telegrambot.service.user.token.TokenService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void getById_WhenUserExists_ReturnsUser() {
        Long userId = 1L;
        AppUser expectedUser = new AppUser();
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        AppUser result = userService.getById(userId);

        assertEquals(expectedUser, result);
        verify(appUserRepository).findById(userId);
    }

    @Test
    void getById_WhenUserNotExists_ThrowsException() {
        Long userId = 99L;
        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
        verify(appUserRepository).findById(userId);
    }

    @Test
    void getByUsername_WhenUserExists_ReturnsUser() {
        String username = "testUser";
        AppUser expectedUser = new AppUser();
        when(appUserRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        AppUser result = userService.getByUsername(username);

        assertEquals(expectedUser, result);
        verify(appUserRepository).findByUsername(username);
    }

    @Test
    void getByUsername_WhenUserNotExists_ThrowsException() {
        String username = "unknown";
        when(appUserRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getByUsername(username));
        verify(appUserRepository).findByUsername(username);
    }

    @Test
    @DisplayName("register valid request")
    void register_WhenValidRequest_SavesUser() {
        RegistrationRequest request = new RegistrationRequest(
                "newUser", "password", "email@test.com");

        AppUser mappedUser = new AppUser();
        String encodedPassword = "encodedPass";
        String generatedToken = "uniqueToken123";

        when(appUserRepository.existsByUsername(request.username())).thenReturn(false);
        when(userMapper.toModel(request)).thenReturn(mappedUser);
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(tokenService.generateToken()).thenReturn(generatedToken);

        userService.register(request);

        verify(appUserRepository).existsByUsername(request.username());
        verify(userMapper).toModel(request);
        verify(passwordEncoder).encode(request.password());
        verify(tokenService).generateToken();

        assertEquals(encodedPassword, mappedUser.getPassword());
        assertEquals(generatedToken, mappedUser.getTelegramToken());
        verify(appUserRepository).save(mappedUser);
    }

    @Test
    @DisplayName("register username already exists exception")
    void register_WhenUsernameExists_ThrowsException() {
        RegistrationRequest request = new RegistrationRequest(
                "existingUser", "pass", "email@test.com");

        when(appUserRepository.existsByUsername(request.username())).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class,
                () -> userService.register(request));

        verify(appUserRepository, never()).save(any());
    }

    @Test
    @DisplayName("register jwtToken generation exception")
    void register_WhenTokenGenerationFails_ThrowsException() {
        RegistrationRequest request = new RegistrationRequest(
                "user", "pass", "email@test.com");

        when(appUserRepository.existsByUsername(request.username())).thenReturn(false);
        when(userMapper.toModel(request)).thenReturn(new AppUser());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPass");
        when(tokenService.generateToken()).thenReturn("duplicateToken");
        when(appUserRepository.existsByTelegramToken("duplicateToken")).thenReturn(true);

        assertThrows(TokenGenerationException.class,
                () -> userService.register(request));

        verify(tokenService, atLeastOnce()).generateToken();
        verify(appUserRepository, never()).save(any());
    }

    @Test
    @DisplayName("register another exception")
    void register_WhenUnexpectedError_ThrowsUserRegistrationException() {
        RegistrationRequest request = new RegistrationRequest(
                "user", "pass", "email@test.com");

        when(appUserRepository.existsByUsername(request.username())).thenReturn(false);
        when(userMapper.toModel(request)).thenThrow(new RuntimeException("DB error"));

        assertThrows(UserRegistrationException.class,
                () -> userService.register(request));

        verify(appUserRepository, never()).save(any());
    }
}
