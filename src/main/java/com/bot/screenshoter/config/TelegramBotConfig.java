package com.bot.screenshoter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotConfig {

    @Value("${WEBHOOK_PATH:${telegram.webhook-path}}")
    private String webhookPath;
    @Value("${BOT_USERNAME:${telegram.bot-username}}")
    private String botUsername;
    @Value("${BOT_TOKEN:${telegram.bot-token}}")
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
