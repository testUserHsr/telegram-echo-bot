package ru.tech.telegrambot.bot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tech.telegrambot.bot.events.MessageReceivedEvent;
import ru.tech.telegrambot.bot.util.BotMessages;
import ru.tech.telegrambot.bot.util.ErrorType;
import ru.tech.telegrambot.gateway.TelegramGateway;

/**
 * Handles and processes bot errors and command exceptions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ErrorHandlingServiceImpl implements ErrorHandlingService {
    private final ObjectProvider<TelegramGateway> gatewayProvider;
    private final ErrorClassificationService errorClassificationService;
    private final BotMessages botMessages;

    /**
     * Handles generic bot errors from updates.
     */
    @Override
    public void handleBotError(Update update, Exception e) {
        logError("Telegram bot error", extractChatId(update), null, e);
        if (update.hasMessage()) {
            sendErrorMessage(update.getMessage().getChatId(), ErrorType.BOT_INTERNAL);
        }
    }

    /**
     * Handles command execution errors with classification.
     */
    @Override
    public void handleCommandError(MessageReceivedEvent event, Exception e) {
        ErrorType type = errorClassificationService.classify(e);
        logError("Command execution failed", event.chatId(), event.text(), e);
        sendErrorMessage(event.chatId(), type);
    }

    /**
     * Sends error message to user with fallback logging.
     */
    private void sendErrorMessage(Long chatId, ErrorType type) {
        try {
            String message = resolveErrorMessage(type);
            TelegramGateway gateway = gatewayProvider.getObject();
            gateway.sendMessage(chatId, message);
        } catch (Exception ex) {
            log.warn("Failed to send error message to chat {}", chatId, ex);
        }
    }

    /**
     * Resolves localized error message by type.
     */
    private String resolveErrorMessage(ErrorType type) {
        return switch (type) {
            case INVALID_TOKEN -> botMessages.getBindErrorInvalidToken();
            case CHAT_ALREADY_BOUND -> botMessages.getBindErrorAlreadyBound();
            case BOT_INTERNAL -> botMessages.getInternalBotError();
            default -> botMessages.getInternalError();
        };
    }

    private Long extractChatId(Update update) {
        return update.hasMessage() ? update.getMessage().getChatId() : null;
    }

    private void logError(String context, Long chatId, String commandText, Exception e) {
        log.error("{} [chatId={}, command='{}']: {}",
                context,
                chatId,
                commandText,
                e.getMessage(),
                e
        );
    }
}
