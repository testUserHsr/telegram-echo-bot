CREATE TABLE user_messages
(
    id        BIGSERIAL PRIMARY KEY,
    text      VARCHAR(200)             NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    author_id BIGINT                   NOT NULL,
    CONSTRAINT fk_user_message_author
        FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE INDEX idx_user_message_timestamp ON user_messages (timestamp DESC);
CREATE INDEX idx_user_message_author_id ON user_messages (author_id);