package com.bot.screenshoter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramBotConfig {

    @Value("${telegram.webhook-path}")
    private String webhookPath;
    @Value("${telegram.bot-username}")
    private String botUsername;
    @Value("${telegram.bot-token}")
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
