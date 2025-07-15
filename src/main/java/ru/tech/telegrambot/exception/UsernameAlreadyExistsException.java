package ru.tech.telegrambot.exception;

import java.util.Map;

/**
 * Thrown when attempting to register or update a user with an existing username.
 */
public class UsernameAlreadyExistsException extends BusinessException {
    private static final String MESSAGE_TEMPLATE = "Username '%s' is already taken";

    private UsernameAlreadyExistsException(String message, Map<String, Object> context) {
        super(message, context);
    }

    /**
     * Creates exception for duplicate username case.
     *
     * @param username the requested username that already exists
     */
    public static UsernameAlreadyExistsException byUsername(String username) {
        return new UsernameAlreadyExistsException(
                String.format(MESSAGE_TEMPLATE, username),
                Map.of("username", username)
        );
    }
}
