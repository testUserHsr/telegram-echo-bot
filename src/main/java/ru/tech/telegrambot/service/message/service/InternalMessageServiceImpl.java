package ru.tech.telegrambot.service.message.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.model.entity.UserMessage;
import ru.tech.telegrambot.repository.UserMessageRepository;

import java.time.Clock;

/**
 * Service for internal message persistence operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InternalMessageServiceImpl implements InternalMessageService {
    private final UserMessageRepository messageRepository;
    private final Clock clock;

    /**
     * Retrieves paginated messages for a user.
     *
     * @param userId   ID of the message author
     * @param pageable pagination configuration
     * @return page of user messages
     */
    @Transactional(readOnly = true)
    @Override
    public Page<UserMessage> getUserMessages(Long userId, Pageable pageable) {
        return messageRepository.findAllByAuthorId(userId, pageable);
    }

    /**
     * Persists a new message with timestamp.
     *
     * @param user message author
     * @param text message content
     */
    @Transactional
    @Override
    public void save(AppUser user, String text) {
        log.debug("Saving message for user: {}", user.getId());
        messageRepository.save(UserMessage.builder()
                .author(user)
                .text(text)
                .timestamp(clock.instant())
                .build());
    }
}
