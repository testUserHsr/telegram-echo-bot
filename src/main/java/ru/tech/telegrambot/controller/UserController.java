package ru.tech.telegrambot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.tech.telegrambot.controller.swagger.UserControllerApi;
import ru.tech.telegrambot.model.dto.message.MessageResponse;
import ru.tech.telegrambot.model.dto.token.TelegramTokenResponse;
import ru.tech.telegrambot.model.entity.UserMessage;
import ru.tech.telegrambot.security.dto.AppUserDetails;
import ru.tech.telegrambot.service.message.service.MessageApiService;
import ru.tech.telegrambot.service.user.service.TelegramUserService;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {
    private final TelegramUserService telegramUserService;
    private final MessageApiService messageApiService;

    @Override
    @GetMapping("/me/messages")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<MessageResponse>> getMyMessages(
            @PageableDefault(size = 10, sort = UserMessage.Fields.timestamp, direction = DESC) Pageable pageable,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(
                messageApiService.getUserMessages(userDetails.userId(), pageable)
        );
    }

    @Override
    @GetMapping("/{userId}/messages")
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.userId")
    public ResponseEntity<Page<MessageResponse>> getUserMessages(
            @PageableDefault(size = 10, sort = UserMessage.Fields.timestamp, direction = DESC) Pageable pageable,
            @PathVariable Long userId) {
        return ResponseEntity.ok(messageApiService.getUserMessages(userId, pageable));
    }

    @Override
    @GetMapping("/me/token")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<TelegramTokenResponse> getToken(
            @AuthenticationPrincipal AppUserDetails userDetails) {
        return ResponseEntity.ok(
                telegramUserService.getTelegramTokenById(userDetails.userId())
        );
    }
}
