package ru.tech.telegrambot.model.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * Telegram echo message request DTO.
 */
@Schema(description = "Request for sending an echo message to Telegram")
public record EchoMessageRequest(
        @Schema(
                description = "Text content to echo back",
                example = "Hello, world!",
                requiredMode = REQUIRED,
                maxLength = MAX_TEXT_SIZE
        )
        @NotBlank
        @Size(max = MAX_TEXT_SIZE,
                message = "Field text must be less than" + MAX_TEXT_SIZE)
        String text
) {
    private static final int MAX_TEXT_SIZE = 200;
}
