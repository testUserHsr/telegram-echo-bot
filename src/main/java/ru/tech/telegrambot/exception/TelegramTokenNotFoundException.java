package ru.tech.telegrambot.exception;

import java.util.Map;

/**
 * Thrown when attempting to access a Telegram token for user that hasn't configured it.
 */
public class TelegramTokenNotFoundException extends BusinessException {
    private static final String MESSAGE_TEMPLATE = "Telegram token not configured for user ID: %d";

    private TelegramTokenNotFoundException(String message, Map<String, Object> context) {
        super(message, context);
    }

    /**
     * Creates exception for missing Telegram token case.
     *
     * @param userId the ID of user without configured token
     * @return exception instance with user context
     */
    public static TelegramTokenNotFoundException byId(Long userId) {
        return new TelegramTokenNotFoundException(
                String.format(MESSAGE_TEMPLATE, userId),
                Map.of("userId", userId)
        );
    }
}
