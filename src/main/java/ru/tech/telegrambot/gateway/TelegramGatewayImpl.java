package ru.tech.telegrambot.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tech.telegrambot.bot.core.TelegramBot;
import ru.tech.telegrambot.exception.TelegramApiGatewayException;

/**
 * Gateway for sending messages through Telegram Bot API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramGatewayImpl implements TelegramGateway {
    private final TelegramBot bot;

    /**
     * Sends text message to specified chat.
     *
     * @param chatId target chat identifier
     * @param text   message content
     * @throws TelegramApiGatewayException if message delivery fails
     */
    @Override
    public void sendMessage(Long chatId, String text) {
        log.debug("Sending message to chat {}: {}", chatId, text);
        SendMessage message = new SendMessage(chatId.toString(), text);
        try {
            bot.execute(message);
            log.trace("Message sent successfully");
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat {}", chatId, e);
            throw TelegramApiGatewayException.byChatId(chatId, e);
        }
    }
}
