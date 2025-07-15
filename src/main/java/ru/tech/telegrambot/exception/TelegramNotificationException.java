package ru.tech.telegrambot.exception;

import java.util.Map;

/**
 * Thrown when message delivery fails during Telegram notification process.
 */
public class TelegramNotificationException extends BusinessException {
    private static final String MESSAGE_TEMPLATE = "Failed to send Telegram notification for user ID: %d";

    private TelegramNotificationException(String message, Map<String, Object> context, Throwable cause) {
        super(message, context, cause);
    }

    /**
     * Creates exception for failed notification attempt.
     *
     * @param userId target user identifier
     * @param cause  original Telegram API exception
     * @return configured exception instance
     */
    public static TelegramNotificationException create(Long userId, Throwable cause) {
        return new TelegramNotificationException(
                String.format(MESSAGE_TEMPLATE, userId),
                Map.of("userId", userId),
                cause
        );
    }
}
