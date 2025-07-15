package ru.tech.telegrambot.service.message.telegram;

import ru.tech.telegrambot.exception.TelegramNotificationException;
import ru.tech.telegrambot.exception.TelegramNotBoundException;
import ru.tech.telegrambot.model.dto.message.EchoMessageRequest;

/**
 * Service for sending messages through Telegram platform.
 */
public interface TelegramMessageService {
    /**
     * Sends echo message to user's Telegram chat.
     *
     * @param userId  ID of the target user
     * @param request Contains message text and metadata
     * @throws TelegramNotBoundException if user has no linked Telegram account
     * @throws TelegramNotificationException   if message delivery fails
     */
    void sendEchoMessage(Long userId, EchoMessageRequest request);
}
