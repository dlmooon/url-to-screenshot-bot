package com.bot.screenshoter.bot;

import com.bot.screenshoter.bot.handlers.impl.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TelegramBot extends SpringWebhookBot {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private String botWebhookPath;
    private String botUsername;
    private String botToken;

    @Autowired
    private UpdateHandler updateHandler;

    public TelegramBot(SetWebhook setWebhook) {
        super(setWebhook);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        executorService.submit(() -> updateHandler.handle(update));
        return null;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return botWebhookPath;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setBotPath(String botWebhookPath) {
        this.botWebhookPath = botWebhookPath;
    }
}
