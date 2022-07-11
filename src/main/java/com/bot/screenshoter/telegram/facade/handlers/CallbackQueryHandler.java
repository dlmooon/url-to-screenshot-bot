package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.WebScreenshoter;
import com.bot.screenshoter.constants.InlineButtonNameEnum;
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

    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        String chatID = callbackQuery.getMessage().getChatId().toString();
        InlineButtonNameEnum button = InlineButtonNameEnum.convert(callbackQuery.getData());
        deleteInlineKeyboard(chatID, callbackQuery.getMessage().getMessageId());
        switch (button) {
            case SIMPLE_SCREENSHOT_BUTTON:
                sendSimpleScreenshot(chatID, urlCache.getRequestUrl(chatID));
                return null;

            case LONG_SCREENSHOT_BUTTON:
                sendLongScreenshot(chatID, urlCache.getRequestUrl(chatID));
                return null;

            case CUSTOM_SCREENSHOT_BUTTON:
                sendCustomScreenshot(chatID, urlCache.getRequestUrl(chatID));
                return null;

            default:
                return new SendMessage(chatID, "Что-то пошло не так, попробуйте еще раз");
        }
    }

    private void deleteInlineKeyboard(String chatID, Integer messageID) {
        DeleteMessage message = new DeleteMessage(chatID, messageID);
        bot.deleteMessage(message);
    }

    private void sendSimpleScreenshot(String chatID, String url) {
        SendDocument document = new SendDocument();
        document.setChatId(chatID);
        document.setDocument(new InputFile(webScreenshoter.takeSimpleScreenshot(url)));
        bot.sendDocument(document);
    }

    private void sendLongScreenshot(String chatID, String url) {
        SendDocument document = new SendDocument();
        document.setChatId(chatID);
        document.setDocument(new InputFile(webScreenshoter.takeLongScreenshot(url)));
        bot.sendDocument(document);
    }

    private void sendCustomScreenshot(String chatID, String url) {
        SendDocument document = new SendDocument();
        document.setChatId(chatID);
        document.setDocument(new InputFile(webScreenshoter.takeCustomScreenshot(url, new Dimension(900, 550))));
        bot.sendDocument(document);
    }
}
