package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.LocaleMessageService;
import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class MessageHandler {

    @Autowired
    InlineKeyboardMaker inlineKeyboardMaker;

    @Autowired
    BotStateCache stateCache;

    @Autowired
    RequestUrlCache urlCache;

    @Autowired
    RequestDimensionCache dimensionCache;

    @Autowired
    LocaleMessageService localeMessage;

    public BotApiMethod<?> processMessage(Message message) {
        return processMessageFromReplyKeyboard(message);
    }

    private BotApiMethod<?> processMessageFromReplyKeyboard(Message message) {
        String chatId = message.getChatId().toString();
        String text = message.getText();

        if (text.equals(localeMessage.getById(chatId, "button_take_screenshot"))) {
            stateCache.setUsersBotState(chatId, BotStateEnum.ASK_URL);
            return new SendMessage(chatId, localeMessage.getById(chatId, "enter_url"));
        } else if (text.equals(localeMessage.getById(chatId, "button_about"))) {
            stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_ABOUT);
            return new SendMessage(chatId, localeMessage.getById(chatId, "about_bot"));
        } else if (text.equals(localeMessage.getById(chatId, "button_language"))) {
            stateCache.setUsersBotState(chatId, BotStateEnum.ASK_LANGUAGE);
            SendMessage message1 = new SendMessage(chatId, localeMessage.getDefault("choose_lang"));
            message1.setReplyMarkup(inlineKeyboardMaker.getKeyboardForSelectLang());
            return message1;
        } else {
            return processMessageFromUser(message);
        }
    }

    private BotApiMethod<?> processMessageFromUser(Message message) {
        String chatId = message.getChatId().toString();
        BotStateEnum botState = stateCache.getUsersBotState(chatId);

        switch (botState) {
            case ASK_URL:
                if (!isCorrectUrl(message.getText())) {
                    return new SendMessage(chatId, localeMessage.getById(chatId, "invalid_url"));
                }
                urlCache.addRequestUrl(chatId, message.getText());

                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_TYPE_SCREENSHOT);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(localeMessage.getById(chatId, "select_screenshot_type"));
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(inlineKeyboardMaker.getKeyboardForSelectTypeScreenshot(chatId));

                return sendMessage;

            case ASK_DIMENSION:
                if (isCorrectFormat(message.getText())) {
                    Dimension dimension = getDimension(message.getText());
                    if (!isCorrectDimension(dimension)) {
                        return new SendMessage(chatId, localeMessage.getById(chatId, "wrong_resolution"));
                    }
                    dimensionCache.addRequestDimension(chatId, dimension);

                    stateCache.setUsersBotState(chatId, BotStateEnum.CONFIRM_ACTION);
                    SendMessage message1 = new SendMessage(chatId, localeMessage.getById(chatId, new String[]{String.valueOf(dimension.getWidth()), String.valueOf(dimension.getHeight())}, "confirm_action"));
                    message1.setReplyMarkup(inlineKeyboardMaker.getKeyboardForConfirmOrCancel(chatId));
                    return message1;
                } else {
                    return new SendMessage(chatId, localeMessage.getById(chatId, "invalid_resolution"));
                }

            case ASK_LANGUAGE:
                SendMessage message1 = new SendMessage(chatId, localeMessage.getDefault("choose_lang"));
                message1.setReplyMarkup(inlineKeyboardMaker.getKeyboardForSelectLang());
                return message1;

            default:
                return new SendMessage(chatId, localeMessage.getById(chatId, "dont_understand"));
        }
    }

    private boolean isCorrectFormat(String text) {
        return text.matches("\\b\\d+\\h[xXхХ]\\h\\d+\\b");
    }

    private boolean isCorrectDimension(Dimension dimension) {
        int min = 1;
        int max = 6000;
        return dimension.getWidth() >= min &&
                dimension.getHeight() >= min &&
                dimension.getWidth() <= max &&
                dimension.getHeight() <= max;
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

    private Dimension getDimension(String text) {
        String[] mas = text.split("[xXхХ]");
        int width = Integer.parseInt(mas[0].trim());
        int height = Integer.parseInt(mas[1].trim());
        return new Dimension(width, height);
    }
}
