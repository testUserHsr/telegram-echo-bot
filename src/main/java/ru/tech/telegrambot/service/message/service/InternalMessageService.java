package ru.tech.telegrambot.service.message.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.model.entity.UserMessage;

/**
 * Service for internal message storage and retrieval operations.
 */
public interface InternalMessageService {
    /**
     * Retrieves paginated messages for a specific user.
     *
     * @param userId   the ID of the user whose messages to retrieve
     * @param pageable pagination and sorting configuration
     * @return page of user messages with metadata
     */
    Page<UserMessage> getUserMessages(Long userId, Pageable pageable);

    /**
     * Stores a new message with automatic timestamp.
     *
     * @param user the author of the message
     * @param text the content of the message
     */
    void save(AppUser user, String text);
}
