package ru.tech.telegrambot.service.user.token;

/**
 * Service for generating unique user tokens.
 */
public interface TokenService {
    /**
     * @return generated unique token string
     */
    String generateToken();
}
