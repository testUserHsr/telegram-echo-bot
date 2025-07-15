package ru.tech.telegrambot.service.message.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.tech.telegrambot.model.dto.message.MessageResponse;

/**
 * Provides API operations for message management
 */
public interface MessageApiService {
    /**
     * Retrieves paginated messages for a specific user
     *
     * @param userId   the ID of the user to retrieve messages for
     * @param pageable pagination information (page number, size, and sorting)
     * @return a paginated list of {@link MessageResponse} objects
     */
    Page<MessageResponse> getUserMessages(Long userId, Pageable pageable);
}
