package ru.tech.telegrambot.service.message.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tech.telegrambot.exception.TelegramNotificationException;
import ru.tech.telegrambot.exception.TelegramNotBoundException;
import ru.tech.telegrambot.exception.UserNotFoundException;
import ru.tech.telegrambot.gateway.TelegramGateway;
import ru.tech.telegrambot.model.dto.message.EchoMessageRequest;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.service.message.service.InternalMessageService;
import ru.tech.telegrambot.service.user.service.UserService;

/**
 * Handles Telegram message operations including echo functionality.
 */
@Service
@Slf4j
public class TelegramMessageServiceImpl implements TelegramMessageService {
    private final UserService userService;
    private final InternalMessageService internalMessageService;
    private final TelegramGateway telegramGateway;
    private final String echoTemplate;

    /**
     * @param userService            User management service
     * @param internalMessageService Message persistence service
     * @param telegramGateway        Telegram API gateway
     * @param echoTemplate           Message template with %s placeholders (name, text)
     */

    public TelegramMessageServiceImpl(
            UserService userService,
            InternalMessageService internalMessageService,
            TelegramGateway telegramGateway,
            @Value("${app.api.messages.echo}") String echoTemplate) {
        this.userService = userService;
        this.internalMessageService = internalMessageService;
        this.telegramGateway = telegramGateway;
        this.echoTemplate = echoTemplate;
    }

    /**
     * Processes and sends echo message to user's Telegram.
     *
     * @param userId  Target user ID
     * @param request Echo message content
     * @throws TelegramNotBoundException     if user has no linked Telegram
     * @throws TelegramNotificationException if delivery fails
     */
    @Override
    public void sendEchoMessage(Long userId, EchoMessageRequest request) {
        log.debug("Sending echo to user: {}", userId);
        try {
            AppUser user = userService.getById(userId);

            validateUser(user);

            internalMessageService.save(user, request.text());

            telegramGateway.sendMessage(
                    user.getTelegramChatId(),
                    createEchoMessage(request.text(), user.getShortName())
            );
            log.info("Echo sent to user: {}", userId);
        } catch (TelegramNotBoundException | UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw TelegramNotificationException.create(userId, e);
        }
    }

    /**
     * Validates user has active Telegram connection.
     *
     * @throws TelegramNotBoundException if chat ID not set
     */
    private void validateUser(AppUser user) {
        if (user.getTelegramChatId() == null) {
            throw TelegramNotBoundException.byId(user.getId());
        }
    }

    /**
     * Formats echo message using template.
     *
     * @return Formatted message string
     */
    private String createEchoMessage(String text, String shortName) {
        return String.format(echoTemplate, shortName, text);
    }
}
