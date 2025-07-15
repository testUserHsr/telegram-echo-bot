package ru.tech.telegrambot.bot.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tech.telegrambot.bot.events.MessageReceivedEvent;
import ru.tech.telegrambot.bot.util.BotMessages;
import ru.tech.telegrambot.gateway.TelegramGateway;

@Component
@RequiredArgsConstructor
public class UnhandledCommandHandler implements CommandHandler {
    private final TelegramGateway gateway;
    private final BotMessages botMessages;

    @Override
    public boolean canHandle(String message) {
        return false;
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        gateway.sendMessage(event.chatId(), botMessages.getUnknownCommand());
    }
}
