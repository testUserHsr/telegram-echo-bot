package ru.tech.telegrambot.gateway;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.tech.telegrambot.bot.core.TelegramBot;
import ru.tech.telegrambot.exception.TelegramApiGatewayException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TelegramGatewayImplTest {

    @InjectMocks
    private TelegramGatewayImpl telegramGateway;

    @Mock
    private TelegramBot bot;

    @Test
    void sendMessage_ShouldExecuteRequest() throws TelegramApiException {
        Long chatId = 12345L;
        String text = "Test message";
        SendMessage expectedMessage = new SendMessage(chatId.toString(), text);

        telegramGateway.sendMessage(chatId, text);

        verify(bot).execute(expectedMessage);
    }

    @Test
    void sendMessage_WhenApiFails_ShouldThrowWrappedException() throws TelegramApiException {
        Long chatId = 12345L;
        String text = "Test message";
        TelegramApiException apiException = new TelegramApiException("API error");

        doThrow(apiException).when(bot).execute(any(SendMessage.class));

        TelegramApiGatewayException exception = assertThrows(
                TelegramApiGatewayException.class,
                () -> telegramGateway.sendMessage(chatId, text)
        );

        assertEquals(chatId, exception.getContext().get("chatId"));
        assertSame(apiException, exception.getCause());
    }
}
