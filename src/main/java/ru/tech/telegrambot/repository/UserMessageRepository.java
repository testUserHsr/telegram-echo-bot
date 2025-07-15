package ru.tech.telegrambot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tech.telegrambot.model.entity.UserMessage;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Long> {
    @EntityGraph(attributePaths = {UserMessage.Fields.author})
    Page<UserMessage> findAllByAuthorId(Long authorId, Pageable pageable);
}
