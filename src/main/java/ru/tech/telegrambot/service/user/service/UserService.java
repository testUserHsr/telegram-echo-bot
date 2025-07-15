package ru.tech.telegrambot.service.user.service;

import ru.tech.telegrambot.exception.UserNotFoundException;
import ru.tech.telegrambot.exception.UserRegistrationException;
import ru.tech.telegrambot.exception.UsernameAlreadyExistsException;
import ru.tech.telegrambot.security.dto.RegistrationRequest;
import ru.tech.telegrambot.model.entity.AppUser;

/**
 * Provides operations for user management.
 */
public interface UserService {
    /**
     * Finds user by ID.
     *
     * @throws UserNotFoundException if user not found
     */
    AppUser getById(Long userId);

    /**
     * Finds user by username/
     *
     * @throws UserNotFoundException if user not found
     */
    AppUser getByUsername(String username);

    /**
     * Registers new user/
     *
     * @throws UsernameAlreadyExistsException if username is taken
     * @throws UserRegistrationException      if registration fails
     */
    void register(RegistrationRequest user);
}
