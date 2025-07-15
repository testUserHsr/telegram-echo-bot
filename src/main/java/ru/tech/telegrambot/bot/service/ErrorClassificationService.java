package ru.tech.telegrambot.bot.service;

import ru.tech.telegrambot.bot.util.ErrorType;

/**
 * Service for classifying exceptions into application-specific error types.
 */
public interface ErrorClassificationService {
    /**
     * Maps an exception to a standardized error type.
     *
     * @param e the exception to classify
     * @return existing ErrorType, never null
     */
    ErrorType classify(Exception e);
}
