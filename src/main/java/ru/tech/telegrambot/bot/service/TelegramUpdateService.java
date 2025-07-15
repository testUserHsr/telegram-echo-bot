package ru.tech.telegrambot.bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Service for processing incoming Telegram updates.
 */
public interface TelegramUpdateService {
    /**
     * Processes a single Telegram update.
     */
    void handleUpdate(Update update);
}
