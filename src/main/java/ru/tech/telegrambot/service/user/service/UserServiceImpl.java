package ru.tech.telegrambot.service.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tech.telegrambot.exception.TokenGenerationException;
import ru.tech.telegrambot.exception.UserNotFoundException;
import ru.tech.telegrambot.exception.UserRegistrationException;
import ru.tech.telegrambot.exception.UsernameAlreadyExistsException;
import ru.tech.telegrambot.security.dto.RegistrationRequest;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.model.mapper.UserMapper;
import ru.tech.telegrambot.repository.AppUserRepository;
import ru.tech.telegrambot.service.user.token.TokenService;

/**
 * Service implementation for user management operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final AppUserRepository appUserRepository;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * @param userId user ID to search
     * @return found user entity
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "usersById", key = "#userId")
    @Override
    public AppUser getById(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));
    }

    /**
     * @param username username to search
     * @return found user entity
     * @throws UserNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "usersByUsername", key = "#username")
    @Override
    public AppUser getByUsername(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.byUsername(username));
    }

    /**
     * Registers new user with encoded password and generated Telegram token.
     *
     * @param registrationRequest user registration data
     * @throws UsernameAlreadyExistsException if username taken
     * @throws TokenGenerationException       if failed to generate unique token
     * @throws UserRegistrationException      for other registration failures
     */
    @Transactional
    @Override
    public AppUser register(RegistrationRequest registrationRequest) {
        log.info("Registering new user: {}", registrationRequest.username());
        try {
            checkDuplicateUsername(registrationRequest.username());

            AppUser user = userMapper.toModel(registrationRequest);
            user.setPassword(passwordEncoder.encode(registrationRequest.password()));
            user.setTelegramToken(generateUniqueToken());

            appUserRepository.save(user);
            log.info("User {} registered successfully", user.getUsername());
            return user;
        } catch (UsernameAlreadyExistsException | TokenGenerationException e) {
            throw e;
        } catch (Exception e) {
            throw UserRegistrationException.create(e);
        }
    }

    /**
     * Checks if username already exists.
     *
     * @throws UsernameAlreadyExistsException if username taken
     */
    private void checkDuplicateUsername(String username) {
        if (appUserRepository.existsByUsername(username)) {
            throw UsernameAlreadyExistsException.byUsername(username);
        }
    }

    /**
     * Generates unique Telegram token with collision handling.
     *
     * @return unique token string
     * @throws TokenGenerationException if fails after max attempts
     */
    private String generateUniqueToken() {
        int maxAttempts = 3;
        int attempt = 0;
        String token;

        while (attempt < maxAttempts) {
            attempt++;
            token = tokenService.generateToken();
            if (!appUserRepository.existsByTelegramToken(token)) {
                return token;
            }
            log.warn("Token collision detected, attempt {} of {}", attempt, maxAttempts);
        }

        throw TokenGenerationException.create(maxAttempts);
    }
}
