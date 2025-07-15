package ru.tech.telegrambot.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * User registration request DTO.
 */
@Schema(description = "User registration data")
public record RegistrationRequest(
        @NotBlank(message = "Field username must be filled!")
        @Size(max = MAX_USERNAME_SIZE,
                message = "Field username must be less than " + MAX_USERNAME_SIZE)
        @Schema(description = "Unique username for login",
                example = "user",
                maxLength = MAX_USERNAME_SIZE)
        String username,

        @NotBlank(message = "Field password must be filled!")
        @Size(max = MAX_PASSWORD_SIZE,
                message = "Field password must be less than " + MAX_PASSWORD_SIZE)
        @Schema(description = "Account password",
                example = "pass",
                maxLength = MAX_PASSWORD_SIZE)
        String password,

        @NotBlank(message = "Field username must be filled!")
        @Size(max = MAX_SHORT_NAME_SIZE,
                message = "Field shortName must be less than " + MAX_SHORT_NAME_SIZE)
        @Schema(description = "User's display name",
                example = "John D.",
                maxLength = MAX_SHORT_NAME_SIZE)
        String shortName
) {
    private static final int MAX_USERNAME_SIZE = 20;
    private static final int MAX_SHORT_NAME_SIZE = 20;
    private static final int MAX_PASSWORD_SIZE = 20;
}
