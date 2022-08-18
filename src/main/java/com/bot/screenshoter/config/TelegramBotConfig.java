package com.bot.screenshoter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotConfig {

    @Value("${WEBHOOK_PATH}")
    private String webhookPath;
    @Value("${BOT_USERNAME}")
    private String botUsername;
    @Value("${BOT_TOKEN}")
    private String botToken;

    public String getWebhookPath() {
        return webhookPath;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
