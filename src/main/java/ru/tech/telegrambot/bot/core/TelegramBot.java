package ru.tech.telegrambot.bot.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tech.telegrambot.bot.service.ErrorHandlingService;
import ru.tech.telegrambot.bot.service.TelegramUpdateService;

/**
 * Main Telegram bot implementation handling incoming updates and errors.
 */
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final TelegramUpdateService updateReceiver;
    private final ErrorHandlingService exceptionHandler;

    /**
     * @param botToken         Telegram bot token from @BotFather
     * @param exceptionHandler Service for processing errors
     * @param botUsername      Public bot username
     * @param updateReceiver   Service for processing valid updates
     */
    public TelegramBot(
            @Value("${app.telegram.token}") String botToken,
            ErrorHandlingService exceptionHandler,
            @Value("${app.telegram.name}") String botUsername,
            TelegramUpdateService updateReceiver) {
        super(botToken);
        this.exceptionHandler = exceptionHandler;
        this.botUsername = botUsername;
        this.updateReceiver = updateReceiver;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    /**
     * Processes incoming updates with error handling.
     *
     * @param update Raw Telegram update object
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            updateReceiver.handleUpdate(update);
        } catch (Exception e) {
            exceptionHandler.handleBotError(update, e);
        }
    }
}
