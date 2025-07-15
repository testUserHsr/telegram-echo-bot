package ru.tech.telegrambot.service.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tech.telegrambot.model.dto.message.MessageResponse;
import ru.tech.telegrambot.model.entity.UserMessage;
import ru.tech.telegrambot.model.mapper.MessageMapper;

/**
 * Service for handling message-related API operations.
 */
@Service
@RequiredArgsConstructor
public class MessageApiServiceImpl implements MessageApiService {
    private final InternalMessageService internalMessageService;
    private final MessageMapper messageMapper;

    /**
     * Retrieves paginated messages for specified user.
     *
     * @param userId   ID of the user whose messages to retrieve
     * @param pageable Pagination configuration (page number, size, sorting)
     * @return Page of message DTOs
     */
    @Transactional(readOnly = true)
    @Override
    public Page<MessageResponse> getUserMessages(Long userId, Pageable pageable) {
        Page<UserMessage> messagePage = internalMessageService.getUserMessages(userId, pageable);
        return messagePage.map(messageMapper::toDto);
    }
}
