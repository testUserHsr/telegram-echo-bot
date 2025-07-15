package ru.tech.telegrambot.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tech.telegrambot.bot.events.MessageReceivedEvent;

/**
 * Handles incoming Telegram updates and converts them to application events.
 */
@Component
@RequiredArgsConstructor
public class TelegramUpdateServiceImpl implements TelegramUpdateService {
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Processes Telegram update and publishes event for text messages.
     *
     * @param update incoming Telegram update object
     */
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            eventPublisher.publishEvent(createEvent(update.getMessage()));
        }
    }

    /**
     * Creates message received event from Telegram message.
     *
     * @param message Telegram message object
     * @return created application event
     */
    private MessageReceivedEvent createEvent(Message message) {
        return new MessageReceivedEvent(
                message.getChatId(),
                message.getText(),
                message.getFrom()
        );
    }
}
