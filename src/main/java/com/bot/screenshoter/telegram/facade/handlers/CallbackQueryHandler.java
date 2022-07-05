package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.WebScreenshoter;
import com.bot.screenshoter.constants.InlineButtonNameEnum;
import com.bot.screenshoter.keyboards.ReplyKeyboardMaker;
import com.bot.screenshoter.repositories.RequestUrlCache;
import com.bot.screenshoter.telegram.UrlToScreenshotBot;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Component
public class CallbackQueryHandler {

    @Lazy
    @Autowired
    UrlToScreenshotBot bot;

    @Autowired
    WebScreenshoter webScreenshoter;

    @Autowired
    RequestUrlCache urlCache;

    @Autowired
    ReplyKeyboardMaker replyKeyboardMaker;

    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        String chatID = callbackQuery.getMessage().getChatId().toString();
        InlineButtonNameEnum button = InlineButtonNameEnum.convert(callbackQuery.getData());
        deleteInlineKeyboard(chatID, callbackQuery.getMessage().getMessageId());
        switch (button) {
            case SIMPLE_SCREENSHOT_BUTTON:
                sendSimpleScreenshot(chatID, callbackQuery.getFrom().getId());
                return null;

            case LONG_SCREENSHOT_BUTTON:
                sendLongScreenshot(chatID, callbackQuery.getFrom().getId());
                return null;

            case CUSTOM_SCREENSHOT_BUTTON:
//                sendCustomScreenshot(chatID, callbackQuery.getFrom().getId());
                // TODO: 30.06.2022 кастомный скриншот работает немного неправильно, а именно, после него размер браузера не приходит в обратное состояние
                return null;

            default:
                return new SendMessage(chatID, "Я не понимаю");
        }
    }

    private void deleteInlineKeyboard(String chatID, Integer messageID) {
        DeleteMessage message = new DeleteMessage(chatID, messageID);
        bot.deleteMessage(message);
    }

    private void sendSimpleScreenshot(String chatID, Long userID) {
        SendDocument document = new SendDocument();
        document.setChatId(chatID);
        document.setDocument(new InputFile(webScreenshoter.takeSimpleScreenshot(urlCache.getRequestUrl(chatID))));
        bot.sendDocument(document);
    }

    private void sendLongScreenshot(String chatID, Long userID) {
        SendDocument document = new SendDocument();
        document.setChatId(chatID);
        document.setDocument(new InputFile(webScreenshoter.takeLongScreenshot(urlCache.getRequestUrl(chatID))));
        bot.sendDocument(document);
    }

    private void sendCustomScreenshot(String chatID, Long userID) {
        SendDocument document = new SendDocument();
        document.setChatId(chatID);
        document.setDocument(new InputFile(webScreenshoter.takeCustomScreenshot(urlCache.getRequestUrl(chatID), new Dimension(900, 550))));
        bot.sendDocument(document);
    }
}
