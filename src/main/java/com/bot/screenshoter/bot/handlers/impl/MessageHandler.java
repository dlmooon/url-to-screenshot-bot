package com.bot.screenshoter.bot.handlers.impl;

import com.bot.screenshoter.bot.handlers.Handler;
import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestPageLoadTimeoutCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import com.bot.screenshoter.services.TelegramService;
import com.bot.screenshoter.utils.DimensionUtils;
import com.bot.screenshoter.utils.UrlUtils;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageHandler implements Handler {

    @Autowired
    InlineKeyboardMaker inlineKeyboardMaker;
    @Autowired
    BotStateCache stateCache;
    @Autowired
    RequestPageLoadTimeoutCache timeoutCache;
    @Autowired
    RequestUrlCache urlCache;
    @Autowired
    RequestDimensionCache dimensionCache;
    @Autowired
    TelegramService telegramService;
    @Autowired
    DimensionUtils dimensionUtils;
    @Autowired
    UrlUtils urlUtils;

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        BotStateEnum botState = stateCache.getUsersBotState(chatId);

        switch (botState) {
            case ASK_URL:
                if (!urlUtils.isCorrectUrl(message.getText())) {
                    telegramService.sendMessage(chatId, "invalid_url");
                    break;
                }
                urlCache.addRequestUrl(chatId, message.getText());

                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_TYPE_SCREENSHOT);
                telegramService.sendMessage(chatId, "select_screenshot_type", inlineKeyboardMaker.getKeyboardForSelectTypeScreenshot(chatId));
                break;

            case ASK_DIMENSION:
                if (dimensionUtils.isCorrectFormatOfDimension(message.getText())) {
                    Dimension dimension = dimensionUtils.getDimension(message.getText());
                    if (!dimensionUtils.isCorrectDimension(dimension)) {
                        telegramService.sendMessage(chatId, "wrong_resolution");
                        break;
                    }
                    dimensionCache.addRequestDimension(chatId, dimension);
                    telegramService.sendMessage(chatId, "add_page_load_timeout?", inlineKeyboardMaker.getKeyboardForConfirmOrCancel(chatId));
                } else {
                    telegramService.sendMessage(chatId, "invalid_resolution");
                }
                break;

            case ASK_PAGE_LOAD_TIMEOUT:
                int timeout = Integer.parseInt(message.getText());
                if (timeout >= 0 && timeout <= 5) {
                    timeoutCache.addRequestPageLoadTimeout(chatId, timeout);
                    stateCache.setUsersBotState(chatId, BotStateEnum.CONFIRM_ACTION);
                    Dimension dimension = dimensionCache.getRequestDimension(chatId);
                    telegramService.sendMessage(chatId, "confirm_action",
                            inlineKeyboardMaker.getKeyboardForTakeCustomScreenshotOrCancel(chatId),
                            String.valueOf(dimension.getWidth()),
                            String.valueOf(dimension.getHeight()),
                            String.valueOf(timeout));
                } else {
                    telegramService.sendMessage(chatId, "wrong_page_load_timeout");
                }
                break;

            case ASK_LANGUAGE:
                telegramService.sendMessage(chatId, "choose_lang", inlineKeyboardMaker.getKeyboardForSelectLang());
                break;

            case TAKING_SCREENSHOT:
                telegramService.sendMessage(chatId, "please_wait");
                break;

            default:
                telegramService.sendMessage(chatId, "dont_understand");
        }
    }
}
