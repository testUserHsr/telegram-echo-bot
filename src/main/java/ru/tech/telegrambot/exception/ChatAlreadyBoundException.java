package ru.tech.telegrambot.exception;

import java.util.Map;

/**
 * Thrown when attempting to bind a Telegram chat that's already linked to another user account.
 */
public class ChatAlreadyBoundException extends BusinessException {
    private static final String MESSAGE_TEMPLATE = "Telegram chat (ID: %d) is already bound to user account (ID: %d)";

    private ChatAlreadyBoundException(String message, Map<String, Object> context) {
        super(message, context);
    }

    /**
     * Creates exception for already bound chat.
     *
     * @param command the operation that triggered the check
     * @param chatId  the Telegram chat ID in conflict
     * @param userId  the existing user ID this chat is bound to
     * @return configured exception instance with conflict details
     */
    public static ChatAlreadyBoundException create(String command, Long chatId, Long userId) {
        return new ChatAlreadyBoundException(
                String.format(MESSAGE_TEMPLATE, chatId, userId),
                Map.of("command", command, "chatId", chatId, "userId", userId)
        );
    }
}