package ru.tech.telegrambot.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Authentication response containing JWT token.
 *
 * @param jwtToken JSON Web Token for authenticated requests
 */
@Schema(description = "Authentication response with JWT token")
public record AuthResponse(
        @Schema(description = "Signed JWT token",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                requiredMode = Schema.RequiredMode.REQUIRED)
        String jwtToken
) {
}
