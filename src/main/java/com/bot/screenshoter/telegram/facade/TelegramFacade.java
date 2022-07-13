package com.bot.screenshoter.telegram.facade;

import com.bot.screenshoter.telegram.facade.handlers.CallbackQueryHandler;
import com.bot.screenshoter.telegram.facade.handlers.CommandHandler;
import com.bot.screenshoter.telegram.facade.handlers.MessageHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@AllArgsConstructor
public class TelegramFacade {

    MessageHandler messageHandler;
    CommandHandler commandHandler;
    CallbackQueryHandler callbackQueryHandler;

    public BotApiMethod<?> handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallback(callbackQuery);
        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.getText().startsWith("/")) {
                return commandHandler.processCommand(message);
            } else {
                return messageHandler.processMessage(message);
            }
        }

        log.warn("Update with id {} does not contain callbackQuery and message", update.getUpdateId());
        return null;
    }
}
