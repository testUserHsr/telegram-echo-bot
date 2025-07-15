package ru.tech.telegrambot.exception;

import java.util.Map;

/**
 * Thrown when attempting to perform Telegram-related operations for user
 * without Telegram chat id.
 */
public class TelegramNotBoundException extends BusinessException {
    private static final String ID_MESSAGE = "Telegram account not bound for user with ID: %d";

    private TelegramNotBoundException(String message, Map<String, Object> context) {
        super(message, context);
    }

    /**
     * Creates exception for missing Telegram binding case.
     *
     * @param userId the ID of user without linked Telegram account
     * @return exception instance with user context
     */
    public static TelegramNotBoundException byId(Long userId) {
        return new TelegramNotBoundException(
                String.format(ID_MESSAGE, userId),
                Map.of("userId", userId)
        );
    }
}
