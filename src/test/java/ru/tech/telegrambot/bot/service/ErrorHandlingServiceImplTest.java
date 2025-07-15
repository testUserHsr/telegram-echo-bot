package ru.tech.telegrambot.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tech.telegrambot.bot.events.MessageReceivedEvent;
import ru.tech.telegrambot.bot.util.BotMessages;
import ru.tech.telegrambot.bot.util.ErrorType;
import ru.tech.telegrambot.exception.TelegramApiGatewayException;
import ru.tech.telegrambot.gateway.TelegramGateway;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorHandlingServiceImplTest {
    @InjectMocks
    private ErrorHandlingServiceImpl errorHandlingService;
    @Mock
    private ObjectProvider<TelegramGateway> gatewayProvider;
    @Mock
    private TelegramGateway telegramGateway;
    @Mock
    private ErrorClassificationService errorClassificationService;
    @Mock
    private BotMessages botMessages;

    private final Long testChatId = 12345L;

    @Test
    void handleBotError_WithTextMessage_ShouldSendInternalError() {
        Update update = createUpdateWithText(testChatId);
        when(gatewayProvider.getObject()).thenReturn(telegramGateway);
        when(botMessages.getInternalBotError()).thenReturn("Server error");

        errorHandlingService.handleBotError(update, new RuntimeException("DB down"));

        verify(telegramGateway).sendMessage(testChatId, "Server error");
    }

    @Test
    void handleBotError_WithoutMessage_ShouldDoNothing() {
        Update update = new Update();

        errorHandlingService.handleBotError(update, new RuntimeException());

        verifyNoInteractions(gatewayProvider, telegramGateway);
    }

    @Test
    void handleCommandError_ShouldSendClassifiedError() {
        MessageReceivedEvent event = new MessageReceivedEvent(testChatId, "/start", new User());
        when(errorClassificationService.classify(any()))
                .thenReturn(ErrorType.INVALID_TOKEN);
        when(gatewayProvider.getObject()).thenReturn(telegramGateway);
        when(botMessages.getBindErrorInvalidToken()).thenReturn("Invalid jwtToken");

        errorHandlingService.handleCommandError(event, new RuntimeException());

        verify(telegramGateway).sendMessage(testChatId, "Invalid jwtToken");
    }

    @Test
    void handleCommandError_WithUnclassifiedError_ShouldSendDefaultMessage() {
        MessageReceivedEvent event = new MessageReceivedEvent(testChatId, "/start", new User());
        when(errorClassificationService.classify(any()))
                .thenReturn(ErrorType.UNKNOWN_EXCEPTION);
        when(gatewayProvider.getObject()).thenReturn(telegramGateway);
        when(botMessages.getInternalError()).thenReturn("Something went wrong");

        errorHandlingService.handleCommandError(event, new NullPointerException());

        verify(telegramGateway).sendMessage(testChatId, "Something went wrong");
    }

    @Test
    void handleCommandError_WhenSendFails_ShouldNotPropagateException() {
        MessageReceivedEvent event = new MessageReceivedEvent(testChatId, "/start", new User());
        when(errorClassificationService.classify(any()))
                .thenReturn(ErrorType.BOT_INTERNAL);
        when(gatewayProvider.getObject()).thenReturn(telegramGateway);
        when(botMessages.getInternalBotError()).thenReturn("Error");
        doThrow(TelegramApiGatewayException.byChatId(testChatId, new TelegramApiException("API down")))
                .when(telegramGateway).sendMessage(any(), any());

        assertDoesNotThrow(() ->
                errorHandlingService.handleCommandError(event, new RuntimeException()));
    }


    private Update createUpdateWithText(Long chatId) {
        Message message = new Message();
        message.setChat(new Chat(chatId, "testChat"));
        message.setText("test");

        Update update = new Update();
        update.setMessage(message);
        return update;
    }
}
