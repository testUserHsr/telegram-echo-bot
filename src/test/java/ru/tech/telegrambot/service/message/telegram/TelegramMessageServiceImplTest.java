package ru.tech.telegrambot.service.message.telegram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tech.telegrambot.exception.TelegramNotificationException;
import ru.tech.telegrambot.exception.TelegramApiGatewayException;
import ru.tech.telegrambot.exception.TelegramNotBoundException;
import ru.tech.telegrambot.gateway.TelegramGateway;
import ru.tech.telegrambot.model.dto.message.EchoMessageRequest;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.service.message.service.InternalMessageService;
import ru.tech.telegrambot.service.user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramMessageServiceImplTest {

    private TelegramMessageServiceImpl telegramMessageServiceImpl;

    @Mock
    private UserService userService;

    @Mock
    private InternalMessageService internalMessageService;

    @Mock
    private TelegramGateway telegramGateway;

    private final String validTemplate = "ECHO [%s]: %s";
    private final Long testUserId = 1L;
    private final String testText = "Hello";
    private final String testShortName = "test_user";
    private final Long testChatId = 123456L;

    @BeforeEach
    void setUp() {
        telegramMessageServiceImpl = new TelegramMessageServiceImpl(
                userService,
                internalMessageService,
                telegramGateway,
                validTemplate
        );
    }

    @Test
    void sendEchoMessage_withValidData_shouldSaveAndSend() {
        AppUser testUser = new AppUser();
        testUser.setId(testUserId);
        testUser.setTelegramChatId(testChatId);
        testUser.setShortName(testShortName);
        EchoMessageRequest request = new EchoMessageRequest(testText);

        when(userService.getById(testUserId)).thenReturn(testUser);

        telegramMessageServiceImpl.sendEchoMessage(testUserId, request);

        verify(internalMessageService).save(testUser, testText);
        verify(telegramGateway).sendMessage(eq(testChatId),
                contains(String.format("[%s]: %s", testShortName, testText)));
    }

    @Test
    void sendEchoMessage_WhenNoTelegramChat_ShouldThrow() {
        AppUser userWithoutChat = AppUser.builder()
                .id(testUserId)
                .telegramChatId(null)
                .build();
        EchoMessageRequest request = new EchoMessageRequest(testText);

        when(userService.getById(testUserId)).thenReturn(userWithoutChat);

        assertThrows(TelegramNotBoundException.class, () ->
                telegramMessageServiceImpl.sendEchoMessage(testUserId, request));

        verify(internalMessageService, never()).save(any(), any());
        verify(telegramGateway, never()).sendMessage(anyLong(), anyString());
    }

    @Test
    void sendEchoMessage_WhenTelegramFails_ShouldWrapException() {
        AppUser testUser = AppUser.builder()
                .id(testUserId)
                .telegramChatId(testChatId)
                .shortName(testShortName)
                .build();
        EchoMessageRequest request = new EchoMessageRequest(testText);

        when(userService.getById(testUserId)).thenReturn(testUser);
        doThrow(TelegramApiGatewayException.byChatId(testChatId, new TelegramApiException("API error")))
                .when(telegramGateway).sendMessage(anyLong(), anyString());

        TelegramNotificationException exception = assertThrows(TelegramNotificationException.class, () ->
                telegramMessageServiceImpl.sendEchoMessage(testUserId, request));

        assertEquals(exception.getContext().get("userId"), testUserId);
        assertThat(exception.getCause()).isInstanceOf(TelegramApiGatewayException.class);

        verify(internalMessageService).save(testUser, testText);
    }

    @Test
    void sendEchoMessage_whenNoTelegramChat_shouldThrow() {
        AppUser userWithoutChat = new AppUser();
        userWithoutChat.setId(testUserId);
        userWithoutChat.setTelegramChatId(null);
        EchoMessageRequest request = new EchoMessageRequest(testText);

        when(userService.getById(testUserId)).thenReturn(userWithoutChat);

        assertThrows(TelegramNotBoundException.class, () ->
                telegramMessageServiceImpl.sendEchoMessage(testUserId, request));

        verify(internalMessageService, never()).save(any(), any());
        verify(telegramGateway, never()).sendMessage(anyLong(), anyString());
    }
}
