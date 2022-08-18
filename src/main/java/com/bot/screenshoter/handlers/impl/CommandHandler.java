package com.bot.screenshoter.handlers.impl;

import com.bot.screenshoter.MessageSender;
import com.bot.screenshoter.handlers.Handler;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import com.bot.screenshoter.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CommandHandler implements Handler {

    @Autowired
    private InlineKeyboardMaker keyboardMaker;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private MessageSender messageSender;

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().startsWith("/");
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String command = message.getText();

        switch (command) {
            case "/start":
                usersRepo.registerOrUpdateIfExist(message.getFrom());
                messageSender.send(chatId, "choose_lang", keyboardMaker.getKeyboardForSelectLang());
                break;

            case "/help":
                messageSender.send(chatId, "help");
                break;

            default:
                messageSender.send(chatId, "unknown_command", command);
        }
    }
}
