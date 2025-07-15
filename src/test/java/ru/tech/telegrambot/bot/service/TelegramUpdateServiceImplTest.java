package ru.tech.telegrambot.bot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.tech.telegrambot.bot.events.MessageReceivedEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TelegramUpdateServiceImplTest {
    @InjectMocks
    private TelegramUpdateServiceImpl telegramUpdateService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void handleUpdate_WhenTextMessage_ShouldPublishEvent() {
        Message message = new Message();
        message.setText("Test message");
        message.setChat(new Chat(123L, "testChat"));
        message.setFrom(new User());

        Update update = new Update();
        update.setMessage(message);

        ArgumentCaptor<MessageReceivedEvent> eventCaptor =
                ArgumentCaptor.forClass(MessageReceivedEvent.class);

        telegramUpdateService.handleUpdate(update);

        verify(eventPublisher).publishEvent(eventCaptor.capture());

        MessageReceivedEvent event = eventCaptor.getValue();
        assertEquals(123L, event.chatId());
        assertEquals("Test message", event.text());
        assertNotNull(event.telegramUser());
    }

    @Test
    void handleUpdate_WhenNonTextMessage_ShouldDoNothing() {
        Update update = new Update();
        update.setMessage(new Message());

        telegramUpdateService.handleUpdate(update);

        verifyNoInteractions(eventPublisher);
    }

    @Test
    void handleUpdate_WhenNoMessage_ShouldDoNothing() {
        Update update = new Update();

        telegramUpdateService.handleUpdate(update);

        verifyNoInteractions(eventPublisher);
    }
}
