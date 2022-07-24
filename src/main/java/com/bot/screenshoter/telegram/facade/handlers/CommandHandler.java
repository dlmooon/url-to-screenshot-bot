package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.keyboards.ReplyKeyboardMaker;
import com.bot.screenshoter.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CommandHandler {

    @Autowired
    ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    UsersRepo usersRepo;

    public BotApiMethod<?> processCommand(Message message) {
        String chatId = message.getChatId().toString();
        String inputText = message.getText();

        switch (inputText) {
            case "/start":
                usersRepo.register(message.getFrom());
                SendMessage sendMessage = new SendMessage(chatId, "Привет");
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMainKeyboard());
                sendMessage.enableMarkdown(true);
                return sendMessage;

            case "/help":
                SendMessage message1 = new SendMessage(chatId, "В данный момент этот раздел находится в разработке. \n\n_По вопросам писать @listener69_");
                message1.enableMarkdown(true);
                return message1;

            default:
                return new SendMessage(chatId, "Неизвестная команда: " + message.getText() + ".\n" +
                        "Используйте /help для получения актуальных команд.");
        }
    }
}
