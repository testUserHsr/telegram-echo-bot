package ru.tech.telegrambot.model.dto.util;

import jakarta.validation.constraints.NotBlank;

/**
 * Simple Request message DTO.
 */
public record SimpleRequest(
        @NotBlank(message = "Field message must be filled!")
        String message
) {
}
