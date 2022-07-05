package com.bot.screenshoter.telegram.facade;

import com.bot.screenshoter.telegram.facade.handlers.CallbackQueryHandler;
import com.bot.screenshoter.telegram.facade.handlers.CommandHandler;
import com.bot.screenshoter.telegram.facade.handlers.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramFacade {

    MessageHandler messageHandler;
    CommandHandler commandHandler;
    CallbackQueryHandler callbackQueryHandler;

    @Autowired
    public TelegramFacade(MessageHandler messageHandler, CommandHandler commandHandler, CallbackQueryHandler callbackQueryHandler) {
        this.messageHandler = messageHandler;
        this.commandHandler = commandHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallback(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message.getText().startsWith("/")) {
                return commandHandler.processCommand(message);
            } else {
                return messageHandler.processMessage(message);
            }
        }
    }
}
