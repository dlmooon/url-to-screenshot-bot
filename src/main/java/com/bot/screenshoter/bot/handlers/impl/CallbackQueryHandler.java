package com.bot.screenshoter.bot.handlers.impl;

import com.bot.screenshoter.bot.handlers.Handler;
import com.bot.screenshoter.constants.InlineButtonEnum;
import com.bot.screenshoter.services.TelegramService;
import com.bot.screenshoter.services.actions.impl.SelectConfirmOrCancelAction;
import com.bot.screenshoter.services.actions.impl.SelectLangAction;
import com.bot.screenshoter.services.actions.impl.SelectScreenshotTypeAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
public class CallbackQueryHandler implements Handler {

    @Autowired
    SelectScreenshotTypeAction selectScreenshotTypeAction;
    @Autowired
    SelectLangAction selectLangAction;
    @Autowired
    SelectConfirmOrCancelAction selectConfirmOrCancelAction;
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
        switch (button) {
            case SIMPLE_SCREENSHOT_BUTTON:
            case LONG_SCREENSHOT_BUTTON:
            case CUSTOM_SCREENSHOT_BUTTON:
            case CONFIRM_CUSTOM_SCREENSHOT_BUTTON:
                selectScreenshotTypeAction.handle(chatId, button);
                break;

            case RUSSIA_LANG_BUTTON:
            case ENGLISH_LANG_BUTTON:
                selectLangAction.handle(chatId, button);
                break;

            case CANCEL_BUTTON:
                selectConfirmOrCancelAction.handle(chatId, button);
                break;

            default:
                log.warn("Failed to handle button: {}", button);
                telegramService.sendMessage(chatId, "something_wrong");
        }
    }
}
