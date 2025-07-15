package ru.tech.telegrambot.bot.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tech.telegrambot.bot.events.MessageReceivedEvent;
import ru.tech.telegrambot.bot.util.BotMessages;
import ru.tech.telegrambot.gateway.TelegramGateway;
import ru.tech.telegrambot.service.user.service.TelegramUserService;

/**
 * Handles bot unbind command for linking Telegram accounts with system users.
 */
@Component
public class UnbindCommandHandler implements CommandHandler {
    private final String command;
    private final TelegramUserService telegramUserService;
    private final TelegramGateway gateway;
    private final BotMessages botMessages;

    public UnbindCommandHandler(
            @Value("${app.telegram.commands.unbind}") String command,
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

    @Override
    public void handle(MessageReceivedEvent event) {
        long chatId = event.chatId();
        telegramUserService.unbindTokenFromUser(chatId);
        gateway.sendMessage(chatId, botMessages.getUnbindSuccess());
    }
}
