package ru.tech.telegrambot.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.security.dto.AppUserDetails;
import ru.tech.telegrambot.service.user.service.UserService;

import java.util.Collections;

/**
 * Spring Security UserDetailsService implementation.
 * Loads users from our AppUser service.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    /**
     * Loads user by username and converts to Spring Security UserDetails.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        AppUser user = userService.getByUsername(username);
        return new AppUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(user.getRole().toAuthority())
        );
    }
}
