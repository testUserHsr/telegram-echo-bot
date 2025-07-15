package ru.tech.telegrambot.bot.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BotMessages {
    @Value("${app.telegram.messages.bind.success}")
    private String bindSuccessMessage;
    @Value("${app.telegram.messages.unbind.success}")
    private String unbindSuccessMessage;
    @Value("${app.telegram.messages.error.invalid-token}")
    private String invalidToken;
    @Value("${app.telegram.messages.error.token-already-bound}")
    private String tokenAlreadyBound;
    @Value("${app.telegram.messages.error.internal-error}")
    private String internalError;
    @Value("${app.telegram.messages.error.default-message}")
    private String internalBotError;
    @Value("${app.telegram.messages.error.default-message}")
    private String unknownCommand;

    public String getBindSuccess() {
        return bindSuccessMessage;
    }

    public String getUnbindSuccess() {
        return unbindSuccessMessage;
    }

    public String getUnknownCommand() {
        return unknownCommand;
    }

    public String getBindErrorInvalidToken() {
        return invalidToken;
    }

    public String getBindErrorAlreadyBound() {
        return tokenAlreadyBound;
    }

    public String getInternalError() {
        return internalError;
    }

    public String getInternalBotError() {
        return internalBotError;
    }
}
