CREATE TABLE users
(
    id               BIGSERIAL PRIMARY KEY,
    username         VARCHAR(50)  NOT NULL,
    password         VARCHAR(128) NOT NULL,
    short_name       VARCHAR(30)  NOT NULL,
    role             VARCHAR(20)  NOT NULL,
    telegram_token   VARCHAR(64) UNIQUE,
    telegram_chat_id BIGINT UNIQUE
);

CREATE INDEX idx_user_username ON users (username);
CREATE INDEX idx_user_telegram_token ON users (telegram_token);
CREATE INDEX idx_user_telegram_chat_id ON users (telegram_chat_id);