package ru.tech.telegrambot.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.tech.telegrambot.security.dto.RegistrationRequest;
import ru.tech.telegrambot.model.entity.AppUser;
import ru.tech.telegrambot.security.RoleType;

/**
 * Mapper for working with {@link AppUser} entity.
 *
 * @see RegistrationRequest
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    /**
     * Converts RegistrationRequest to AppUser model.
     * Automatically sets role to {@link RoleType#ROLE_USER} for all new registrations.
     *
     * @param request registration request DTO
     * @return mapped AppUser entity with default USER role
     */
    @Mapping(target = "role", source = "request")
    AppUser toModel(RegistrationRequest request);

    /**
     * Default role mapping strategy.
     * All new users get ROLE_USER by default (security best practice).
     *
     * @param request registration request (unused in default implementation)
     * @return always returns {@link RoleType#ROLE_USER}
     */
    default RoleType mapRole(RegistrationRequest request) {
        return RoleType.ROLE_USER;
    }
}
