package ru.tech.telegrambot.exception;

import java.util.Map;
/**
 * Thrown when Telegram API gateway fails during message delivery.
 */
public class TelegramApiGatewayException extends BusinessException {
    private static final String MESSAGE_TEMPLATE = "Failed to send Telegram message to chat ID: %d";

    private TelegramApiGatewayException(String message, Map<String, Object> context, Throwable cause) {
        super(message, context, cause);
    }

    /**
     * Creates exception for failed message delivery.
     *
     * @param chatId target chat identifier
     * @param cause  root Telegram API exception
     * @return configured exception instance
     */
    public static TelegramApiGatewayException byChatId(Long chatId, Throwable cause) {
        return new TelegramApiGatewayException(
                String.format(MESSAGE_TEMPLATE, chatId),
                Map.of("chatId", chatId),
                cause
        );
    }
}