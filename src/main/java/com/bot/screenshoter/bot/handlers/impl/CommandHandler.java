package com.bot.screenshoter.bot.handlers.impl;

import com.bot.screenshoter.bot.handlers.Handler;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import com.bot.screenshoter.repository.UsersRepo;
import com.bot.screenshoter.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

@Service
public class CommandHandler implements Handler {

    @Autowired
    private InlineKeyboardMaker keyboardMaker;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private TelegramService telegramService;

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
                telegramService.sendMessage(chatId, "choose_lang", keyboardMaker.getKeyboardForSelectLang());
                break;

            case "/help":
                telegramService.sendPhoto(chatId, "help", new InputFile(new File("src/main/resources/botpic.png")));
                break;

            default:
                telegramService.sendMessage(chatId, "unknown_command", command);
        }
    }
}
