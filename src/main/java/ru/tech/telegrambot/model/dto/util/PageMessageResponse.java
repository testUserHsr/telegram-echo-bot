package ru.tech.telegrambot.model.dto.util;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.tech.telegrambot.model.dto.message.MessageResponse;

import java.util.List;

@Schema(description = "Paginated response wrapper")
public record PageMessageResponse(
        @Schema(description = "List of message items")
        List<MessageResponse> content,

        @Schema(description = "Current page number (0-based)")
        int number,

        @Schema(description = "Number of items per page")
        int size,

        @Schema(description = "Total number of items across all pages")
        long totalElements,

        @Schema(description = "Total number of pages")
        int totalPages,

        @Schema(description = "Whether this is the first page")
        boolean first,

        @Schema(description = "Whether this is the last page")
        boolean last,

        @Schema(description = "Whether the current page has content")
        boolean empty
) {
}
