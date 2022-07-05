package com.bot.screenshoter.config;

import com.bot.screenshoter.telegram.UrlToScreenshotBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class SpringConfig {
    @Autowired
    private TelegramBotConfig telegramBotConfig;

    @Bean
    public SetWebhook setWebhook() {
        return SetWebhook.builder().url(telegramBotConfig.getWebhookPath()).build();
    }

    @Bean
    public UrlToScreenshotBot springWebhookBot(SetWebhook setWebhook) {
        UrlToScreenshotBot bot = new UrlToScreenshotBot(setWebhook);

        bot.setBotUsername(telegramBotConfig.getBotUsername());
        bot.setBotToken(telegramBotConfig.getBotToken());
        bot.setBotWebhookPath(telegramBotConfig.getWebhookPath());

        return bot;
    }
}
