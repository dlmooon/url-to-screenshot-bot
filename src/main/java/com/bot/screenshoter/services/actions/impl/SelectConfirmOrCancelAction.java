package com.bot.screenshoter.services.actions.impl;

import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonEnum;
import com.bot.screenshoter.services.TelegramService;
import com.bot.screenshoter.services.actions.InlineKeyboardAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectConfirmOrCancelAction implements InlineKeyboardAction {

    @Autowired
    private BotStateCache stateCache;
    @Autowired
    private TelegramService telegramService;

    @Override
    public boolean supports(InlineButtonEnum button) {
        return button.equals(InlineButtonEnum.CANCEL_BUTTON) ||
                button.equals(InlineButtonEnum.CONFIRM_BUTTON);
    }

    @Override
    public void handle(String chatId, InlineButtonEnum button) {
        switch (button) {
            case CONFIRM_BUTTON:
                break;

            case CANCEL_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                telegramService.sendMessage(chatId, "action_canceled");
                break;
        }
    }
}
