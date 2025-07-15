package ru.tech.telegrambot.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.tech.telegrambot.exception.ChatAlreadyBoundException;
import ru.tech.telegrambot.exception.TelegramNotBoundException;
import ru.tech.telegrambot.exception.TelegramNotificationException;
import ru.tech.telegrambot.exception.TelegramTokenNotFoundException;
import ru.tech.telegrambot.exception.TokenGenerationException;
import ru.tech.telegrambot.exception.UserNotFoundException;
import ru.tech.telegrambot.exception.UserRegistrationException;
import ru.tech.telegrambot.exception.UsernameAlreadyExistsException;

import java.util.stream.Collectors;

/**
 * Exception Handler Controller for handle exceptions.
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            WebRequest webRequest
    ) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, webRequest);
    }

    @ExceptionHandler(TelegramNotBoundException.class)
    public ResponseEntity<ErrorResponse> handleTelegramNotBoundException(
            TelegramNotBoundException ex,
            WebRequest webRequest
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), webRequest);
    }

    @ExceptionHandler({AccessDeniedException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleSecurityException(
            RuntimeException ex,
            WebRequest webRequest
    ) {
        HttpStatus status = ex instanceof AuthenticationException
                ? HttpStatus.UNAUTHORIZED
                : HttpStatus.FORBIDDEN;

        return buildResponse(status, ex.getMessage(), webRequest);
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            TelegramTokenNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            RuntimeException ex,
            WebRequest webRequest
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), webRequest);
    }

    @ExceptionHandler({
            ChatAlreadyBoundException.class,
            UsernameAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleConflictException(
            RuntimeException ex,
            WebRequest webRequest
    ) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), webRequest);
    }

    @ExceptionHandler({
            TokenGenerationException.class,
            TelegramNotificationException.class,
            UserRegistrationException.class
    })
    public ResponseEntity<ErrorResponse> handleInternalServerException(
            RuntimeException ex,
            WebRequest webRequest
    ) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), webRequest);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleInfrastructureException(
            DataAccessException ex,
            WebRequest webRequest
    ) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), webRequest);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            WebRequest webRequest
    ) {
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .path(webRequest.getDescription(false)
                        .replace("uri=", ""))
                .build();
        log.error("Error {}: {}", status.value(), response);
        return ResponseEntity.status(status)
                .body(response);
    }
}
