package ru.tech.telegrambot.service.user.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    private TokenServiceImpl tokenService;

    @Mock
    private SecureRandom secureRandom;

    private final int defaultTokenLength = 32;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl(defaultTokenLength, secureRandom);
    }

    @Test
    void generateToken_ShouldReturnValidString() {
        doAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            Arrays.fill(bytes, (byte) 1);
            return null;
        }).when(secureRandom).nextBytes(any(byte[].class));

        String token = tokenService.generateToken();

        assertNotNull(token);
        // Base64 URL-encoded string length calculation
        int expectedLength = (int) Math.ceil(4 * defaultTokenLength / 3.0);
        assertEquals(expectedLength, token.length());
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 24, 32, 64})
    void generateToken_WithDifferentLengths_Parametrized(int length) {
        TokenServiceImpl service = new TokenServiceImpl(length, secureRandom);
        doAnswer(invocation -> {
            byte[] bytes = invocation.getArgument(0);
            Arrays.fill(bytes, (byte) 1);
            return null;
        }).when(secureRandom).nextBytes(any(byte[].class));

        String token = service.generateToken();

        assertNotNull(token);
        // Base64 URL-encoded string length calculation
        int expectedLength = (int) Math.ceil(4 * length / 3.0);
        assertEquals(expectedLength, token.length());
    }
}
