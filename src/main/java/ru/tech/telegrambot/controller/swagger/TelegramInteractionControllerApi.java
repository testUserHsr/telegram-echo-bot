package ru.tech.telegrambot.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tech.telegrambot.controller.handler.ErrorResponse;
import ru.tech.telegrambot.model.dto.message.EchoMessageRequest;
import ru.tech.telegrambot.security.dto.AppUserDetails;

@Tag(name = "Telegram Interaction API", description = "API for Telegram message interactions")
@RequestMapping("/api/v1/telegram/interactions")
public interface TelegramInteractionControllerApi {

    @Operation(
            summary = "Send echo message",
            description = "Sends a message back to the user (echo). Requires USER or ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "202",
            description = "Message accepted for processing"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Bad request (invalid input)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized (invalid or missing jwtToken)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden (no required role)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @PostMapping("/echo")
    ResponseEntity<Void> sendEchoMessage(
            @Parameter(hidden = true) @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody EchoMessageRequest request
    );
}
