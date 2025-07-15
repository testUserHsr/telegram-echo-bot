package ru.tech.telegrambot.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tech.telegrambot.controller.handler.ErrorResponse;
import ru.tech.telegrambot.model.dto.message.MessageResponse;
import ru.tech.telegrambot.model.dto.token.TelegramTokenResponse;
import ru.tech.telegrambot.security.dto.AppUserDetails;

@Tag(name = "User Management API",
        description = "API for managing user data and messages")
@RequestMapping("/api/users")
public interface UserControllerApi {

    @Operation(
            summary = "Get current user's messages",
            description = "Returns paginated list of messages for authenticated user. Requires USER or ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "page",
                            description = "Page number (0-based)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Number of items per page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "10")
                    ),
                    @Parameter(
                            name = "sort",
                            description = "Sorting criteria in format: property(,asc|desc)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", defaultValue = "timestamp,desc")
                    )
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved messages",
            content = @Content(schema = @Schema(implementation = Page.class))
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
    ResponseEntity<Page<MessageResponse>> getMyMessages(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) @AuthenticationPrincipal AppUserDetails userDetails
    );

    @Operation(
            summary = "Get user messages by ID",
            description = "Returns paginated list of messages for specified user. ADMIN can access any user, regular users can only access their own messages.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID of the user whose messages to retrieve",
                            required = true,
                            in = ParameterIn.PATH,
                            schema = @Schema(type = "integer")
                    ),
                    @Parameter(
                            name = "page",
                            description = "Page number (0-based)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "0")
                    ),
                    @Parameter(
                            name = "size",
                            description = "Number of items per page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "10")
                    ),
                    @Parameter(
                            name = "sort",
                            description = "Sorting criteria in format: property(,asc|desc)",
                            in = ParameterIn.QUERY,
                            schema = @Schema(type = "string", defaultValue = "timestamp,desc")
                    )
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved messages",
            content = @Content(schema = @Schema(implementation = Page.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized (invalid or missing jwtToken)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden (no access to this user's messages)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    ResponseEntity<Page<MessageResponse>> getUserMessages(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(hidden = true) Long userId
    );

    @Operation(
            summary = "Get Telegram token",
            description = "Returns token to bind Telegram chat.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved Telegram token",
            content = @Content(schema = @Schema(implementation = TelegramTokenResponse.class))
    )
    @ApiResponse(
            responseCode = "401",
            description = "Unauthorized (invalid or missing token)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "403",
            description = "Forbidden (no required role)",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    ResponseEntity<TelegramTokenResponse> getToken(
            @Parameter(hidden = true) @AuthenticationPrincipal AppUserDetails userDetails
    );
}
