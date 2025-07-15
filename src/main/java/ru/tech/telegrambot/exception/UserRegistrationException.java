package ru.tech.telegrambot.exception;

import java.util.Collections;
import java.util.Map;

/**
 * Thrown when user registration process fails.
 */
public class UserRegistrationException extends BusinessException {
    private static final String MESSAGE_TEMPLATE = "User registration failed";

    private UserRegistrationException(String message, Map<String, Object> context, Throwable cause) {
        super(message, context, cause);
    }

    /**
     * Factory method for creating exception.
     *
     * @param cause root exception that caused registration failure
     * @return configured exception instance
     */
    public static UserRegistrationException create(Throwable cause) {
        return new UserRegistrationException(
                String.format(MESSAGE_TEMPLATE),
                Collections.emptyMap(),
                cause
        );
    }
}
