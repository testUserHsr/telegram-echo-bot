package ru.tech.telegrambot.exception;

import java.util.Map;

/**
 * Thrown when requested user entity cannot be found.
 */
public class UserNotFoundException extends BusinessException {
    private static final String ID_MESSAGE = "User with ID %d not found";
    private static final String USERNAME_MESSAGE = "User with username '%s' not found";
    private static final String TOKEN_MESSAGE = "Invalid Telegram jwtToken";

    private UserNotFoundException(String message, Map<String, Object> context) {
        super(message, context);
    }

    /**
     * Creates exception for missing user ID.
     *
     * @param userId the ID that was not found
     */
    public static UserNotFoundException byId(Long userId) {
        return new UserNotFoundException(
                String.format(ID_MESSAGE, userId),
                Map.of("userId", userId)
        );
    }

    /**
     * Creates exception for missing user token.
     *
     * @param command the command that triggered the check
     * @param token   the invalid token
     */
    public static UserNotFoundException byToken(String command, String token) {
        return new UserNotFoundException(
                TOKEN_MESSAGE,
                Map.of("command", command, "token", token)
        );
    }

    /**
     * Creates exception for missing username.
     *
     * @param username the username that was not found
     */
    public static UserNotFoundException byUsername(String username) {
        return new UserNotFoundException(
                String.format(USERNAME_MESSAGE, username),
                Map.of("username", username)
        );
    }
}
