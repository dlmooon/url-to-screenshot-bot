package com.bot.screenshoter.telegram;

import com.bot.screenshoter.telegram.facade.TelegramFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
public class Bot extends SpringWebhookBot {

    private String botWebhookPath;
    private String botUsername;
    private String botToken;

    @Autowired
    TelegramFacade telegramFacade;

    public Bot(SetWebhook setWebhook) {
        super(setWebhook);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return telegramFacade.handleUpdate(update);
    }

    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message", e);
        }
    }

    public void deleteMessage(DeleteMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("Failed to delete message", e);
        }
    }

    public boolean sendScreenshotAsDocument(SendDocument document) {
        try {
            execute(document);
            return true;
        } catch (TelegramApiException e) {
            log.warn("Failed to send document", e);
            return false;
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @Override
    public String getBotPath() {
        return botWebhookPath;
    }

    public void setBotPath(String botWebhookPath) {
        this.botWebhookPath = botWebhookPath;
    }
}