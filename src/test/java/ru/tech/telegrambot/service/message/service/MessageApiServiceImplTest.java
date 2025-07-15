package ru.tech.telegrambot.service.message.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.tech.telegrambot.model.dto.message.MessageResponse;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.model.entity.UserMessage;
import ru.tech.telegrambot.model.mapper.MessageMapper;
import ru.tech.telegrambot.security.RoleType;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageApiServiceImplTest {

    @InjectMocks
    private MessageApiServiceImpl messageApiService;

    @Mock
    private InternalMessageService internalMessageService;

    @Mock
    private MessageMapper messageMapper;

    private static final Instant TEST_TIMESTAMP = Instant.parse("2023-01-01T00:00:00Z");

    @Test
    void getUserMessages_ShouldReturnMappedResponse() {
        Long testUserId = 1L;
        Pageable testPageable = Pageable.ofSize(10).withPage(0);

        UserMessage userMessage1 = new UserMessage(1L, "Text 1", TEST_TIMESTAMP, createTestUser(testUserId));
        UserMessage userMessage2 = new UserMessage(2L, "Text 2", TEST_TIMESTAMP, createTestUser(testUserId));
        List<UserMessage> messages = List.of(userMessage1, userMessage2);

        Page<UserMessage> mockMessagePage = new PageImpl<>(messages, testPageable, messages.size());

        MessageResponse mockResponse1 = new MessageResponse(1L, "Text 1", TEST_TIMESTAMP, testUserId);
        MessageResponse mockResponse2 = new MessageResponse(2L, "Text 2", TEST_TIMESTAMP, testUserId);

        when(internalMessageService.getUserMessages(testUserId, testPageable))
                .thenReturn(mockMessagePage);
        when(messageMapper.toDto(userMessage1))
                .thenReturn(mockResponse1);
        when(messageMapper.toDto(userMessage2))
                .thenReturn(mockResponse2);

        Page<MessageResponse> resultPage = messageApiService.getUserMessages(testUserId, testPageable);

        verify(internalMessageService).getUserMessages(testUserId, testPageable);

        assertThat(resultPage.getContent())
                .hasSize(2)
                .containsExactly(mockResponse1, mockResponse2);

        assertThat(resultPage.getPageable()).isEqualTo(testPageable);
        assertThat(resultPage.getTotalElements()).isEqualTo(2);
    }

    private AppUser createTestUser(Long testUserId) {
        return new AppUser(
                testUserId,
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
