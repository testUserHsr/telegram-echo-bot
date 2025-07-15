package ru.tech.telegrambot.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.security.RoleType;
import ru.tech.telegrambot.security.dto.AppUserDetails;
import ru.tech.telegrambot.service.user.service.UserService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserService userService;

    @Test
    void loadUserByUsername_WhenUserExists_ReturnsUserDetails() {
        String userUsername = "user";
        AppUser existingUser = new AppUser(
                1L,
                userUsername,
                "encodedPass",
                "shortName",
                RoleType.ROLE_USER,
                "jwtToken",
                null,
                Collections.emptyList()
        );
        when(userService.getByUsername(userUsername)).thenReturn(existingUser);

        var userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(userUsername);

        assertNotNull(userDetails);
        assertEquals(1L, userDetails.userId());
        assertEquals(userUsername, userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().iterator().next().getAuthority().contains("ROLE_USER"));

        verify(userService, times(1)).getByUsername(userUsername);
    }
}
