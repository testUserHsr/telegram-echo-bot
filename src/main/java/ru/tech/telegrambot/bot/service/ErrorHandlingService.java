package ru.tech.telegrambot.bot.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tech.telegrambot.bot.events.MessageReceivedEvent;

public interface ErrorHandlingService {
    void handleBotError(Update update, Exception e);
    void handleCommandError(MessageReceivedEvent event, Exception e);
}
