package com.bot.screenshoter.bot.handlers.impl;

import com.bot.screenshoter.bot.handlers.Handler;
import com.bot.screenshoter.constants.InlineButtonEnum;
import com.bot.screenshoter.services.TelegramService;
import com.bot.screenshoter.services.actions.InlineKeyboardAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Slf4j
@Service
public class CallbackQueryHandler implements Handler {

    @Autowired
    private List<InlineKeyboardAction> inlineActions;
    @Autowired
    private TelegramService telegramService;

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String chatId = callbackQuery.getMessage().getChatId().toString();
        InlineButtonEnum button = InlineButtonEnum.valueOf(callbackQuery.getData());
        telegramService.deleteMessage(chatId, callbackQuery.getMessage().getMessageId());

        inlineActions.stream()
                .filter(inlineKeyboardAction -> inlineKeyboardAction.supports(button))
                .findFirst()
                .ifPresent(inlineKeyboardAction -> inlineKeyboardAction.handle(chatId, button));
    }
}
