package ru.tech.telegrambot.bot.handler;

import ru.tech.telegrambot.bot.events.MessageReceivedEvent;

/**
 * Contract for handling specific bot commands.
 */
public interface CommandHandler {
    /**
     * Determines if this handler can process the given message.
     *
     * @param message raw command text from user
     * @return true if handler supports this command, false otherwise
     */
    boolean canHandle(String message);

    /**
     * Processes the command event.
     *
     * @param message event containing command details and context
     */
    void handle(MessageReceivedEvent message);
}
