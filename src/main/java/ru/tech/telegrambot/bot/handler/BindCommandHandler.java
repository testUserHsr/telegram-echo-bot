package ru.tech.telegrambot.bot.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tech.telegrambot.bot.events.MessageReceivedEvent;
import ru.tech.telegrambot.bot.util.BotMessages;
import ru.tech.telegrambot.gateway.TelegramGateway;
import ru.tech.telegrambot.service.user.service.TelegramUserService;

/**
 * Handles bot bind command for linking Telegram accounts with system users.
 */
@Component
public class BindCommandHandler implements CommandHandler {
    private final String command;
    private final TelegramUserService telegramUserService;
    private final TelegramGateway gateway;
    private final BotMessages botMessages;

    public BindCommandHandler(
            @Value("${app.telegram.commands.bind}") String command,
            TelegramUserService telegramUserService,
            TelegramGateway gateway,
            BotMessages botMessages
    ) {
        this.command = command;
        this.telegramUserService = telegramUserService;
        this.gateway = gateway;
        this.botMessages = botMessages;
    }

    @Override
    public boolean canHandle(String message) {
        return message.startsWith(command);
    }

    /**
     * Executes account binding flow:
     * 1. Extracts token from command
     * 2. Bind Telegram chat with user account
     * 3. Sends success confirmation
     */
    @Override
    public void handle(MessageReceivedEvent event) {
        long chatId = event.chatId();
        String token = extractToken(event.text());
        telegramUserService.bindTokenToUser(token, chatId);
        gateway.sendMessage(chatId, botMessages.getBindSuccess());
    }

    private String extractToken(String text) {
        return text.substring(command.length()).trim();
    }
}
