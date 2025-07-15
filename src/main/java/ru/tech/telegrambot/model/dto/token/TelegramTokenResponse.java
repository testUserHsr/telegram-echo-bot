package ru.tech.telegrambot.model.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Telegram authentication token DTO.
 */
@Schema(description = "Telegram authentication token")
public record TelegramTokenResponse(
        @Schema(description = "Unique token for Telegram authentication",
                example = "0brtQ6NsHTGJ1LHzpbzIsZjigibdPk-GhVTK...")
        String token
) {
}
