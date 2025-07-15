package ru.tech.telegrambot.bot.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.tech.telegrambot.bot.handler.CommandHandler;
import ru.tech.telegrambot.bot.handler.UnhandledCommandHandler;
import ru.tech.telegrambot.bot.service.ErrorHandlingService;

import java.util.List;

/**
 * Routes incoming commands to appropriate handlers based on command text.
 */
@Component
@RequiredArgsConstructor
public class CommandDispatcher {
    private final List<CommandHandler> handlers;
    private final UnhandledCommandHandler defaultHandler;
    private final ErrorHandlingService errorHandler;

    /**
     * Processes message event by finding and executing suitable command handler.
     *
     * @param event incoming command event containing message details
     */
    @EventListener
    public void handle(MessageReceivedEvent event) {
        try {
            CommandHandler handler = findSuitableHandler(event.text());
            handler.handle(event);
        } catch (Exception e) {
            errorHandler.handleCommandError(event, e);
        }
    }

    private CommandHandler findSuitableHandler(String commandText) {
        return handlers.stream()
                .filter(handler -> handler.canHandle(commandText))
                .findFirst()
                .orElse(defaultHandler);
    }
}
