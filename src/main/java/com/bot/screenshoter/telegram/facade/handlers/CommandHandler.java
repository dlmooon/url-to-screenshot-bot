package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.LocaleMessageService;
import com.bot.screenshoter.MessageSender;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import com.bot.screenshoter.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CommandHandler {

    @Autowired
    InlineKeyboardMaker keyboardMaker;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    LocaleMessageService localeMessage;

    @Autowired
    MessageSender messageSender;

    public BotApiMethod<?> processCommand(Message message) {
        String chatId = message.getChatId().toString();
        String command = message.getText();

        switch (command) {
            case "/start":
                if (!usersRepo.isUserExist(message.getFrom().getId())) {
                    usersRepo.register(message.getFrom());
                }
                SendMessage sendMessage = new SendMessage(chatId, localeMessage.getDefault("choose_lang"));
                sendMessage.setReplyMarkup(keyboardMaker.getKeyboardForSelectLang());
                sendMessage.enableMarkdown(true);
                return sendMessage;

            case "/help":
                SendMessage message1 = new SendMessage(chatId, localeMessage.getById(chatId, "help"));
                message1.enableMarkdown(true);
                return message1;

            default:
                if (message.getFrom().getId() == 986497271 && command.equals("/send_all")) {
                    messageSender.sendAllUsersAboutUpdate();
                    return null;
                }
                return new SendMessage(chatId, localeMessage.getById(chatId, new String[]{command}, "unknown_command"));
        }
    }
}
