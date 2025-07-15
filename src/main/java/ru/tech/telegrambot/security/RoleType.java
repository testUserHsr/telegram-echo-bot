package ru.tech.telegrambot.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;

/**
 * Authorization role type.
 */
public enum RoleType implements Serializable {
    ROLE_ADMIN,
    ROLE_USER;

    public GrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}
