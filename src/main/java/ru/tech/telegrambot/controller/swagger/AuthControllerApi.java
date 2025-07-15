package ru.tech.telegrambot.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tech.telegrambot.controller.handler.ErrorResponse;
import ru.tech.telegrambot.security.dto.AuthResponse;
import ru.tech.telegrambot.security.dto.LoginRequest;
import ru.tech.telegrambot.security.dto.RegistrationRequest;
import ru.tech.telegrambot.model.dto.util.SimpleResponse;

@Tag(name = "AuthenticationController",
        description = "User authentication controller.")
@RequestMapping("api/v1/auth")
public interface AuthControllerApi {

    @Operation(
            summary = "Register a new user",
            description = "Endpoint for user registration"
    )
    @ApiResponse(
            responseCode = "201",
            description = "User successfully registered",
            content = @Content(schema = @Schema(implementation = SimpleResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "409",
            description = "User already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    ResponseEntity<SimpleResponse> register(
            @RequestBody @Valid RegistrationRequest registrationRequest);

    @Operation(
            summary = "Authentication  user",
            description = "Endpoint for user authentication"
    )
    @ApiResponse(
            responseCode = "200",
            description = "User successfully registered",
            content = @Content(schema = @Schema(implementation = SimpleResponse.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    ResponseEntity<AuthResponse> authUser(
            @RequestBody @Valid LoginRequest loginRequest);
}
