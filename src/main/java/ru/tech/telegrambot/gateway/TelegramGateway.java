package ru.tech.telegrambot.gateway;

public interface TelegramGateway {
    void sendMessage(Long chatId, String text);
}
