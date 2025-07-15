package ru.tech.telegrambot.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tech.telegrambot.controller.swagger.TelegramInteractionControllerApi;
import ru.tech.telegrambot.model.dto.message.EchoMessageRequest;
import ru.tech.telegrambot.security.dto.AppUserDetails;
import ru.tech.telegrambot.service.message.telegram.TelegramMessageService;

@RestController
@RequiredArgsConstructor
public class TelegramInteractionController implements TelegramInteractionControllerApi {
    private final TelegramMessageService telegramMessageService;

    @Override
    @PostMapping("/echo")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> sendEchoMessage(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody EchoMessageRequest request) {
        telegramMessageService.sendEchoMessage(userDetails.userId(), request);
        return ResponseEntity.accepted().build();
    }
}
