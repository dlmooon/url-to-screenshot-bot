package com.bot.screenshoter.telegram;

import com.bot.screenshoter.handlers.impl.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
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
    UpdateHandler updateHandler;

    public Bot(SetWebhook setWebhook) {
        super(setWebhook);
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        new Thread(() -> updateHandler.handle(update)).start();
        return null;
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

    public void sendBotAction(SendChatAction chatAction) {
        try {
            execute(chatAction);
        } catch (TelegramApiException e) {
            log.warn("Failed to send chat action", e);
        }
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
