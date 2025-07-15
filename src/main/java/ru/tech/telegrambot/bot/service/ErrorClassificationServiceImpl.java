package ru.tech.telegrambot.bot.service;

import org.springframework.stereotype.Service;
import ru.tech.telegrambot.bot.util.ErrorType;
import ru.tech.telegrambot.exception.ChatAlreadyBoundException;
import ru.tech.telegrambot.exception.UserNotFoundException;

import java.util.Map;

/**
 * Maps exceptions to error types for consistent error handling.
 */
@Service
public class ErrorClassificationServiceImpl implements ErrorClassificationService {
    private final Map<Class<? extends Exception>, ErrorType> exceptionToErrorMap;

    public ErrorClassificationServiceImpl() {
        this.exceptionToErrorMap = Map.of(
                UserNotFoundException.class, ErrorType.INVALID_TOKEN,
                ChatAlreadyBoundException.class, ErrorType.CHAT_ALREADY_BOUND
        );
    }

    /**
     * Determines error type based on exception class.
     *
     * @param e Exception to classify
     * @return Corresponding ErrorType or UNKNOWN_EXCEPTION if no mapping exists
     */
    @Override
    public ErrorType classify(Exception e) {
        return exceptionToErrorMap.getOrDefault(
                e.getClass(),
                ErrorType.UNKNOWN_EXCEPTION
        );
    }
}
