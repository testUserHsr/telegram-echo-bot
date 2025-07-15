package ru.tech.telegrambot.service.user.service;

import ru.tech.telegrambot.exception.ChatAlreadyBoundException;
import ru.tech.telegrambot.exception.TelegramTokenNotFoundException;
import ru.tech.telegrambot.exception.UserNotFoundException;
import ru.tech.telegrambot.model.dto.token.TelegramTokenResponse;
import ru.tech.telegrambot.model.entity.AppUser;

import java.util.Optional;

/**
 * Service for managing Telegram-related user associations.
 */
public interface TelegramUserService {
    /**
     * Finds user by their Telegram connection token.
     *
     * @param token Unique Telegram binding token
     * @return Optional containing user if found
     */
    Optional<AppUser> findByTelegramToken(String token);

    /**
     * Binds Telegram chat ID to user account.
     *
     * @param token  User's Telegram connection token
     * @param chatId Telegram chat ID to bind
     * @throws UserNotFoundException     if token is invalid
     * @throws ChatAlreadyBoundException if chat ID is already in use
     */
    void bindTokenToUser(String token, Long chatId);

    /**
     * Removes Telegram chat ID association.
     *
     * @param chatId Telegram chat ID to unbind
     */
    void unbindTokenFromUser(Long chatId);

    /**
     * Retrieves user's Telegram connection token.
     *
     * @param userId Internal user identifier
     * @return Token response DTO
     * @throws TelegramTokenNotFoundException if no token exists
     */
    TelegramTokenResponse getTelegramTokenById(Long userId);
}
