package ru.tech.telegrambot.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serial;
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
@Table(name = "user_messages", indexes = {
        @Index(name = "idx_message_timestamp", columnList = "timestamp DESC")
})
public class UserMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = UserMessage.Fields.id)
    private Long id;

    /**
     * Message text content.
     */
    @Column(name = UserMessage.Fields.text)
    private String text;

    /**
     * Message creation timestamp.
     * Stored with timezone information in database.
     */
    @Column(columnDefinition = "TIMESTAMP(6) WITH TIME ZONE")
    private Instant timestamp;

    /**
     * Author of the message.
     * Many-to-one relationship with {@link AppUser}.
     */
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private AppUser author;
}
