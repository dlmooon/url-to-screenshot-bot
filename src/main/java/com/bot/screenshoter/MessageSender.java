package com.bot.screenshoter;

import com.bot.screenshoter.telegram.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Service
public class MessageSender {

    @Lazy
    @Autowired
    private Bot bot;

    @Autowired
    private LocaleMessageService localeService;

    public void send(String chatId, String text) {
        String localeText = localeService.getById(chatId, text);
        SendMessage message = new SendMessage(chatId, localeText);
        bot.sendMessage(message);
    }

    public void send(String chatId, String text, ReplyKeyboard replyKeyboard) {
        String localeText = localeService.getById(chatId, text);
        SendMessage message = new SendMessage(chatId, localeText);
        message.setReplyMarkup(replyKeyboard);
        bot.sendMessage(message);
    }

    public void send(String chatId, String text, String... args) {
        String localeText = localeService.getById(chatId, args, text);
        SendMessage message = new SendMessage(chatId, localeText);
        bot.sendMessage(message);
    }

    public void send(String chatId, String text, ReplyKeyboard replyKeyboard, String... args) {
        String localeText = localeService.getById(chatId, args, text);
        SendMessage message = new SendMessage(chatId, localeText);
        message.setReplyMarkup(replyKeyboard);
        bot.sendMessage(message);
    }

    public void delete(String chatId, Integer messageId) {
        DeleteMessage message = new DeleteMessage(chatId, messageId);
        bot.deleteMessage(message);
    }
}
