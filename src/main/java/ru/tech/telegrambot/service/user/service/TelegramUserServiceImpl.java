package ru.tech.telegrambot.service.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import ru.tech.telegrambot.exception.ChatAlreadyBoundException;
import ru.tech.telegrambot.exception.TelegramTokenNotFoundException;
import ru.tech.telegrambot.exception.UserNotFoundException;
import ru.tech.telegrambot.model.dto.token.TelegramTokenResponse;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.repository.AppUserRepository;

import java.util.Optional;

/**
 * Service for managing Telegram-related user operations.
 */
@Service
@RequiredArgsConstructor
public class TelegramUserServiceImpl implements TelegramUserService {
    private final AppUserRepository appUserRepository;

    /**
     * Finds user by Telegram token.
     *
     * @return Optional with user if found, empty otherwise
     */
    @Override
    public Optional<AppUser> findByTelegramToken(String token) {
        return appUserRepository.findByTelegramToken(token);
    }

    /**
     * Links Telegram chat ID to user account.
     *
     * @throws UserNotFoundException     if token is invalid
     * @throws ChatAlreadyBoundException if chat ID is already in use
     */
    @Transactional
    @CacheEvict(value = {"usersById", "usersByUsername"}, key = "#result.id")
    @Override
    public AppUser bindTokenToUser(String token, Long chatId) {
        AppUser user = findByTelegramToken(token)
                .orElseThrow(() -> UserNotFoundException.byToken("/bind", token));
        checkDuplicateChatId(chatId, user.getId());
        user.setTelegramChatId(chatId);
        return user;
    }

    /**
     * Checks if chat ID is already bound to another user.
     *
     * @throws ChatAlreadyBoundException if chat ID is occupied
     */
    private void checkDuplicateChatId(Long chatId, Long userId) {
        appUserRepository.findByTelegramChatId(chatId)
                .filter(existingUser -> !existingUser.getId().equals(userId))
                .ifPresent(existingUser -> {
                    throw ChatAlreadyBoundException.create(
                            "/bind", chatId, existingUser.getId()
                    );
                });
    }

    /**
     * Removes Telegram chat ID association from user.
     *
     * @throws UserNotFoundException if chat id is invalid
     */
    @Transactional
    @CacheEvict(value = {"usersById", "usersByUsername"}, key = "#result.id")
    @Override
    public AppUser unbindTokenFromUser(Long chatId) {
        AppUser user = appUserRepository.findByTelegramChatId(chatId)
                .orElseThrow(() -> UserNotFoundException.byChatId(chatId));
        user.setTelegramChatId(null);
        return user;
    }

    /**
     * Gets Telegram token for user.
     *
     * @throws TelegramTokenNotFoundException if token not found
     */
    @Override
    public TelegramTokenResponse getTelegramTokenById(Long userId) {
        return appUserRepository.findTelegramTokenByUserId(userId)
                .map(TelegramTokenResponse::new)
                .orElseThrow(() -> TelegramTokenNotFoundException.byId(userId));
    }
}
