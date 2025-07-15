package ru.tech.telegrambot.controller.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Standard error response for API endpoints.
 */
@Builder
@Schema(description = "Error response format")
public record ErrorResponse(
        @Schema(example = "400", description = "HTTP status code")
        int status,

        @Schema(example = "Invalid input data", description = "Error message")
        String message,

        @Schema(example = "/api/v1/telegram/interactions/echo",
                description = "API endpoint path where error occurred")
        String path
) {
}
