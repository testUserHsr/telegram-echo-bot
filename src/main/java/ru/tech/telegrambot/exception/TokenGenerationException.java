package ru.tech.telegrambot.exception;

import java.util.Map;

/**
 * Thrown when the system fails to generate a unique token after multiple attempts.
 */
public class TokenGenerationException extends BusinessException {
    private static final String MESSAGE_TEMPLATE = "Failed to generate unique User Telegram token after %d attempts";

    private TokenGenerationException(String message, Map<String, Object> context) {
        super(message, context);
    }

    /**
     * Creates an exception for token generation failure.
     *
     * @param maxAttempts the number of unsuccessful generation attempts made
     * @return exception instance with formatted message and attempt count
     */
    public static TokenGenerationException create(int maxAttempts) {
        return new TokenGenerationException(
                String.format(MESSAGE_TEMPLATE, maxAttempts),
                Map.of("maxAttempts", maxAttempts)
        );
    }
}
