package ru.tech.telegrambot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import ru.tech.telegrambot.security.RoleType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an application user with authentication and Telegram integration.
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "users")
public class AppUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(
            name = "users_id_seq",
            sequenceName = "users_id_seq",
            allocationSize = 1
    )
    @Column(name = Fields.id)
    private Long id;

    /**
     * Unique login identifier.
     */
    @Column(name = Fields.username, nullable = false, length = 50)
    private String username;

    /**
     * Password for authentication.
     */
    @Column(name = Fields.password, nullable = false, length = 128)
    private String password;

    /**
     * Display name (max 30 chars) shown in UI.
     */
    @Column(name = "short_name", nullable = false, length = 30)
    private String shortName;

    /**
     * User's role defining access permissions.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = Fields.role, nullable = false, length = 20)
    private RoleType role;

    /**
     * Token for Telegram account binding.
     * Generated on first connection.
     */
    @Column(name = "telegram_token", unique = true, length = 64)
    private String telegramToken;

    /**
     * Unique Telegram chat identifier.
     */
    @Column(name = "telegram_chat_id", unique = true)
    private Long telegramChatId;

    /**
     * User's messages (bi-directional relation).
     */
    @ToString.Exclude
    @OneToMany(mappedBy = UserMessage.Fields.author, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserMessage> messages = new ArrayList<>();
}
