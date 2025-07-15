package ru.tech.telegrambot.model.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * Message response DTO containing message data and metadata.
 */
public record MessageResponse(
        @Schema(description = "Unique message ID", example = "123")
        Long id,

        @Schema(description = "Message text content", example = "Hello world")
        String text,

        @Schema(description = "Creation timestamp", example = "1752503404.173067")
        Instant timestamp,

        @Schema(description = "Author user ID", example = "42")
        Long authorId
) {
}
