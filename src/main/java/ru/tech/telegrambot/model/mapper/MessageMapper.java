package ru.tech.telegrambot.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.tech.telegrambot.model.dto.message.MessageResponse;
import ru.tech.telegrambot.model.entity.UserMessage;

/**
 * Mapper for working with {@link UserMessage} entity.
 *
 * @see MessageResponse
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {
    /**
     * Converts {@link UserMessage} entity to {@link MessageResponse} DTO.
     *
     * @param model source entity to convert
     * @return converted DTO with author ID exposed
     */
    @Mapping(source = "author.id", target = "authorId")
    MessageResponse toDto(UserMessage model);
}
