package ru.tech.telegrambot.service.user.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Generates cryptographically secure random tokens in Base64 URL-safe format.
 */
@Service
public class TokenServiceImpl implements TokenService {
    private final int tokenLength;
    private final SecureRandom secureRandom;

    /**
     * @param tokenLength  length of token in bytes (configurable via app.api.security.token.length)
     * @param secureRandom cryptographically strong random generator
     */
    public TokenServiceImpl(@Value("${app.api.security.token.length:32}") int tokenLength, SecureRandom secureRandom) {
        this.tokenLength = tokenLength;
        this.secureRandom = secureRandom;
    }

    /**
     * Generates URL-safe random token using SecureRandom.
     *
     * @return Base64 encoded token without padding
     */
    @Override
    public String generateToken() {
        byte[] bytes = new byte[tokenLength];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
