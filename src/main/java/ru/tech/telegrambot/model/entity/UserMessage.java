package ru.tech.telegrambot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents a user message entity in the system.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "user_messages")
public class UserMessage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_messages_id_seq")
    @SequenceGenerator(
            name = "user_messages_id_seq",
            sequenceName = "user_messages_id_seq",
            allocationSize = 1
    )
    @Column(name = Fields.id)
    private Long id;

    /**
     * Message text content.
     */
    @Column(name = Fields.text, nullable = false, length = 200)
    private String text;

    /**
     * Message creation timestamp.
     * Stored with timezone information in database.
     */
    @Column(name = Fields.timestamp, nullable = false)
    private Instant timestamp;

    /**
     * Author of the message.
     * Many-to-one relationship with {@link AppUser}.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private AppUser author;
}
