package ru.tech.telegrambot.service.message.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.model.entity.UserMessage;
import ru.tech.telegrambot.security.RoleType;
import ru.tech.telegrambot.repository.UserMessageRepository;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InternalMessageServiceImplTest {

    @InjectMocks
    private InternalMessageServiceImpl messageService;

    @Mock
    private UserMessageRepository messageRepository;

    @Mock
    private Clock clock;

    private final Instant fixedInstant = Instant.parse("2023-01-01T00:00:00Z");

    @Test
    void getUserMessages_WhenCalled_ReturnsPageOfMessages() {
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        Page<UserMessage> expectedPage = mock(Page.class);

        when(messageRepository.findAllByAuthorId(userId, pageable))
                .thenReturn(expectedPage);

        Page<UserMessage> result = messageService.getUserMessages(userId, pageable);

        assertEquals(expectedPage, result);
        verify(messageRepository).findAllByAuthorId(userId, pageable);
    }

    @Test
    void save_WhenCalled_SavesMessageWithCorrectData() {
        AppUser user = createTestUser();
        String messageText = "Test message";

        when(clock.instant()).thenReturn(fixedInstant);

        messageService.save(user, messageText);

        ArgumentCaptor<UserMessage> messageCaptor = ArgumentCaptor.forClass(UserMessage.class);
        verify(messageRepository).save(messageCaptor.capture());

        UserMessage savedMessage = messageCaptor.getValue();
        assertEquals(user, savedMessage.getAuthor());
        assertEquals(messageText, savedMessage.getText());
        assertEquals(fixedInstant, savedMessage.getTimestamp());
    }

    private AppUser createTestUser() {
        return new AppUser(
                1L,
                "user",
                "encodedPass",
                "shortName",
                RoleType.ROLE_USER,
                "jwtToken",
                null,
                Collections.emptyList()
        );
    }
}
