package com.bot.screenshoter.services;

import com.bot.screenshoter.config.TelegramBotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramService extends DefaultAbsSender {

    @Autowired
    private LocaleMessageService localeService;
    @Autowired
    private TelegramBotConfig telegramBotConfig;

    public TelegramService() {
        super(new DefaultBotOptions());
    }

    public void sendMessage(String chatId, String text) {
        String localeText = localeService.getById(chatId, text);
        SendMessage message = new SendMessage(chatId, localeText);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message", e);
        }
    }

    public void sendMessage(String chatId, String text, ReplyKeyboard replyKeyboard) {
        String localeText = localeService.getById(chatId, text);
        SendMessage message = new SendMessage(chatId, localeText);
        message.setReplyMarkup(replyKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message", e);
        }
    }

    public void sendMessage(String chatId, String text, String... args) {
        String localeText = localeService.getById(chatId, args, text);
        SendMessage message = new SendMessage(chatId, localeText);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message", e);
        }
    }

    public void sendMessage(String chatId, String text, ReplyKeyboard replyKeyboard, String... args) {
        String localeText = localeService.getById(chatId, args, text);
        SendMessage message = new SendMessage(chatId, localeText);
        message.setReplyMarkup(replyKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message", e);
        }
    }

    public boolean sendDocument(String chatId, InputFile document) {
        SendDocument sendDocument = new SendDocument(chatId, document);

        try {
            execute(sendDocument);
            return true;
        } catch (TelegramApiException e) {
            log.warn("Failed to send document", e);
            return false;
        }
    }

    public void sendChatAction(String chatId, ActionType actionType) {
        SendChatAction sendChatAction = new SendChatAction(chatId, actionType.toString());

        try {
            execute(sendChatAction);
        } catch (TelegramApiException e) {
            log.warn("Failed to send chat action", e);
        }
    }

    public void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage message = new DeleteMessage(chatId, messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.warn("Failed to delete message", e);
        }
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getBotToken();
    }
}
