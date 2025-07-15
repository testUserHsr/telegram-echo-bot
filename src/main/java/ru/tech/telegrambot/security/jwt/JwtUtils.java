package ru.tech.telegrambot.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Utility class for JWT token operations (generation, parsing, validation).
 */
@Component
@Slf4j
public class JwtUtils {
    private final String jwtSecret;
    private final Duration tokenExpiration;
    private final Clock clock;

    public JwtUtils(
            @Value("${app.api.security.jwt.secret-key}") String jwtSecret,
            @Value("${app.api.security.jwt.expiration}") Duration tokenExpiration,
            Clock clock
    ) {
        this.jwtSecret = jwtSecret;
        this.tokenExpiration = tokenExpiration;
        this.clock = clock;
    }

    /**
     * Generates JWT token from username.
     *
     * @param username subject for token
     * @return signed JWT token
     */
    public String generateTokenFromUsername(String username) {
        Instant now = clock.instant();
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(tokenExpiration)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        log.debug("Generated JWT token for user: {}", username);
        return jwtToken;
    }

    /**
     * Creates signing key from secret.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts username (subject) from JWT token.
     *
     * @throws ExpiredJwtException   if token expired
     * @throws MalformedJwtException if token invalid
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates JWT token structure and signature.
     *
     * @return true if token is valid, false otherwise
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
