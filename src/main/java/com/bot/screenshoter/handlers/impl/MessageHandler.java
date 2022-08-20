package com.bot.screenshoter.handlers.impl;

import com.bot.screenshoter.TelegramSender;
import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.handlers.Handler;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import com.bot.screenshoter.utils.DimensionUtils;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class MessageHandler implements Handler {

    @Autowired
    InlineKeyboardMaker inlineKeyboardMaker;
    @Autowired
    BotStateCache stateCache;
    @Autowired
    RequestUrlCache urlCache;
    @Autowired
    RequestDimensionCache dimensionCache;
    @Autowired
    TelegramSender telegramSender;
    @Autowired
    DimensionUtils dimensionUtils;

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
                if (!isCorrectUrl(message.getText())) {
                    telegramSender.sendMessage(chatId, "invalid_url");
                    break;
                }
                urlCache.addRequestUrl(chatId, message.getText());

                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_TYPE_SCREENSHOT);
                telegramSender.sendMessage(chatId, "select_screenshot_type", inlineKeyboardMaker.getKeyboardForSelectTypeScreenshot(chatId));
                break;

            case ASK_DIMENSION:
                if (dimensionUtils.isCorrectFormatOfDimension(message.getText())) {
                    Dimension dimension = dimensionUtils.getDimension(message.getText());
                    if (!dimensionUtils.isCorrectDimension(dimension)) {
                        telegramSender.sendMessage(chatId, "wrong_resolution");
                        break;
                    }
                    dimensionCache.addRequestDimension(chatId, dimension);

                    stateCache.setUsersBotState(chatId, BotStateEnum.CONFIRM_ACTION);
                    telegramSender.sendMessage(chatId, "confirm_action",
                            inlineKeyboardMaker.getKeyboardForConfirmOrCancel(chatId),
                            String.valueOf(dimension.getWidth()),
                            String.valueOf(dimension.getHeight()));
                } else {
                    telegramSender.sendMessage(chatId, "invalid_resolution");
                }
                break;

            case ASK_LANGUAGE:
                telegramSender.sendMessage(chatId, "choose_lang", inlineKeyboardMaker.getKeyboardForSelectLang());
                break;

            default:
                telegramSender.sendMessage(chatId, "dont_understand");
        }
    }

    private boolean isCorrectUrl(String url) {
        try {
            URL checkUrl = new URL(url);
            checkUrl.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
