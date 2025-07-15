package ru.tech.telegrambot.bot.events;

import org.telegram.telegrambots.meta.api.objects.User;

/**
 * Event representing a message received from Telegram user.
 */
public record MessageReceivedEvent(
        Long chatId,
        String text,
        User telegramUser
) {
}
