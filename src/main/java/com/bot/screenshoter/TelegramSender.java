package com.bot.screenshoter;

import com.bot.screenshoter.telegram.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Service
public class TelegramSender {

    @Lazy
    @Autowired
    private Bot bot;

    @Autowired
    private LocaleMessageService localeService;

    public void sendMessage(String chatId, String text) {
        String localeText = localeService.getById(chatId, text);
        SendMessage message = new SendMessage(chatId, localeText);
        bot.sendMessage(message);
    }

    public void sendMessage(String chatId, String text, ReplyKeyboard replyKeyboard) {
        String localeText = localeService.getById(chatId, text);
        SendMessage message = new SendMessage(chatId, localeText);
        message.setReplyMarkup(replyKeyboard);
        bot.sendMessage(message);
    }

    public void sendMessage(String chatId, String text, String... args) {
        String localeText = localeService.getById(chatId, args, text);
        SendMessage message = new SendMessage(chatId, localeText);
        bot.sendMessage(message);
    }

    public void sendMessage(String chatId, String text, ReplyKeyboard replyKeyboard, String... args) {
        String localeText = localeService.getById(chatId, args, text);
        SendMessage message = new SendMessage(chatId, localeText);
        message.setReplyMarkup(replyKeyboard);
        bot.sendMessage(message);
    }

    public boolean sendDocument(String chatId, InputFile document) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(document);
        return bot.sendDocument(sendDocument);
    }

    public void sendChatAction(String chatId, ActionType actionType) {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(actionType);
        bot.sendChatAction(sendChatAction);
    }

    public void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage message = new DeleteMessage(chatId, messageId);
        bot.deleteMessage(message);
    }
}
