# Telegram Echo Bot API

REST API для приёма сообщений и отправки их в Telegram бот.<br>
Swagger UI доступен по адресу: http://localhost:8088/swagger-ui.html

## How To Use

### Заполнить .env
TELEGRAM_BOT_TOKEN=ваш_токен<br>
TELEGRAM_BOT_USERNAME=имя_бота

### Запуск

```
mvn clean package -DskipTests
docker build -t telegram-echo-bot:latest .
docker compose up
```
