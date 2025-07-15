package ru.tech.telegrambot.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tech.telegrambot.model.entity.AppUser;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    @EntityGraph(attributePaths = {AppUser.Fields.messages})
    Optional<AppUser> findByUsername(String username);

    @EntityGraph(attributePaths = {AppUser.Fields.messages})
    Optional<AppUser> findByTelegramToken(String telegramToken);

    @EntityGraph(attributePaths = {AppUser.Fields.messages})
    Optional<AppUser> findByTelegramChatId(Long chatId);

    @Query("SELECT u.telegramToken FROM AppUser u WHERE u.id = :userId")
    Optional<String> findTelegramTokenByUserId(@Param("userId") Long userId);

    boolean existsByUsername(String username);

    boolean existsByTelegramToken(String token);
}
