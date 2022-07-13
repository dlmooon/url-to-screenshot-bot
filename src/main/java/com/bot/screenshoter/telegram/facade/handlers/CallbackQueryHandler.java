package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.WebScreenshoter;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonNameEnum;
import com.bot.screenshoter.repositories.BotStateRepo;
import com.bot.screenshoter.repositories.RequestDimensionCache;
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

import java.io.File;

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
    RequestDimensionCache dimensionCache;

    @Autowired
    BotStateRepo stateRepo;

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
                stateRepo.setUsersBotState(chatID, BotStateEnum.ASK_DIMENSION);
                return new SendMessage(chatID, "Введите разрешение скриншота в формате:\n" +
                        "ширина x высота\n" +
                        "где ширина и высота - числа от 10 до 6000 включительно\n" +
                        "(пример: 1920 x 800)");

            case CONFIRM_BUTTON:
                sendCustomScreenshot(chatID, urlCache.getRequestUrl(chatID), dimensionCache.getRequestDimension(chatID));
                return null;

            case CANCEL_BUTTON:
                stateRepo.setUsersBotState(chatID, BotStateEnum.SHOW_MENU);
                return new SendMessage(chatID, "Действие отменено");

            default:
                return new SendMessage(chatID, "Что-то пошло не так, попробуйте еще раз");
        }
    }

    private void deleteInlineKeyboard(String chatID, Integer messageID) {
        DeleteMessage message = new DeleteMessage(chatID, messageID);
        bot.deleteMessage(message);
    }

    private void sendSimpleScreenshot(String chatID, String url) {
        File file = webScreenshoter.takeSimpleScreenshot(url);
        if (file == null) {
            bot.sendMessage(new SendMessage(chatID, "Страница не доступна или не существует"));
        } else {
            InputFile screenshot = new InputFile(file);
            sendDocument(chatID, screenshot);
        }
    }

    private void sendLongScreenshot(String chatID, String url) {
        File file = webScreenshoter.takeLongScreenshot(url);
        if (file == null) {
            bot.sendMessage(new SendMessage(chatID, "Страница не доступна или не существует"));
        } else {
            InputFile screenshot = new InputFile(file);
            sendDocument(chatID, screenshot);
        }
    }

    private void sendCustomScreenshot(String chatID, String url, Dimension dimension) {
        File file = webScreenshoter.takeCustomScreenshot(url, dimension);
        if (file == null) {
            bot.sendMessage(new SendMessage(chatID, "Страница не доступна или не существует"));
        } else {
            InputFile screenshot = new InputFile(file);
            sendDocument(chatID, screenshot);
        }
    }

    private void sendDocument(String chatID, InputFile screenshot) {
        SendDocument document = new SendDocument();
        document.setChatId(chatID);
        document.setDocument(screenshot);
        bot.sendDocument(document);
    }
}
