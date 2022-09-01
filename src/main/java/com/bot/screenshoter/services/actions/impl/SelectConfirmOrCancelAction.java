package com.bot.screenshoter.services.actions.impl;

import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonEnum;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import com.bot.screenshoter.services.TelegramService;
import com.bot.screenshoter.services.actions.InlineKeyboardAction;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectConfirmOrCancelAction implements InlineKeyboardAction {

    @Autowired
    private BotStateCache stateCache;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private InlineKeyboardMaker inlineKeyboardMaker;
    @Autowired
    private RequestDimensionCache dimensionCache;

    @Override
    public boolean supports(InlineButtonEnum button) {
        return button.equals(InlineButtonEnum.CANCEL_BUTTON) ||
                button.equals(InlineButtonEnum.CONFIRM_BUTTON);
    }

    @Override
    public void handle(String chatId, InlineButtonEnum button) {
        BotStateEnum state = stateCache.getUsersBotState(chatId);
        switch (button) {
            case CONFIRM_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_PAGE_LOAD_TIMEOUT);
                telegramService.sendMessage(chatId, "enter_page_load_timeout");
                break;

            case CANCEL_BUTTON:
                switch (state) {
                    case CONFIRM_ACTION:
                        stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                        telegramService.sendMessage(chatId, "action_canceled");
                        break;
                    case ASK_DIMENSION:
                        Dimension dimension = dimensionCache.getRequestDimension(chatId);
                        telegramService.sendMessage(chatId, "confirm_action",
                                inlineKeyboardMaker.getKeyboardForTakeCustomScreenshotOrCancel(chatId),
                                String.valueOf(dimension.getWidth()),
                                String.valueOf(dimension.getHeight()),
                                "1");
                        break;
                }
                break;
        }
    }
}
