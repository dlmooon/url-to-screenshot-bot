package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.WebScreenshoter;
import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonNameEnum;
import com.bot.screenshoter.telegram.UrlToScreenshotBot;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    BotStateCache stateRepo;

    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        InlineButtonNameEnum button = InlineButtonNameEnum.convert(callbackQuery.getData());
        deleteInlineKeyboard(chatId, callbackQuery.getMessage().getMessageId());
        switch (button) {
            case SIMPLE_SCREENSHOT_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                sendSimpleScreenshot(chatId, urlCache.getRequestUrl(chatId));
                return null;

            case LONG_SCREENSHOT_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                sendLongScreenshot(chatId, urlCache.getRequestUrl(chatId));
                return null;

            case CUSTOM_SCREENSHOT_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.ASK_DIMENSION);
                return new SendMessage(chatId, "Введите разрешение скриншота, ширина и высота не должны быть больше 6000\n" +
                        "(пример: 1920 x 800)");

            case CONFIRM_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                sendCustomScreenshot(chatId, urlCache.getRequestUrl(chatId), dimensionCache.getRequestDimension(chatId));
                return null;

            case CANCEL_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                return new SendMessage(chatId, "Действие отменено");

            default:
                return new SendMessage(chatId, "Что-то пошло не так, попробуйте еще раз");
        }
    }

    private void deleteInlineKeyboard(String chatId, Integer messageID) {
        DeleteMessage message = new DeleteMessage(chatId, messageID);
        bot.deleteMessage(message);
    }

    private void sendSimpleScreenshot(String chatId, String url) {
        try {
            File file = webScreenshoter.takeSimpleScreenshot(url);
            InputFile screenshot = new InputFile(file);
            sendDocument(chatId, screenshot);
        } catch (RuntimeException e) {
            log.warn("Failed to get simple screenshot", e);
            bot.sendMessage(new SendMessage(chatId, "Не удается получить доступ к сайту, проверьте нет ли опечаток в Url адресе"));
        }
    }

    private void sendLongScreenshot(String chatId, String url) {
        try {
            File file = webScreenshoter.takeLongScreenshot(url);
            InputFile screenshot = new InputFile(file);
            sendDocument(chatId, screenshot);
        } catch (RuntimeException e) {
            log.warn("Failed to get long screenshot", e);
            bot.sendMessage(new SendMessage(chatId, "Не удается получить доступ к сайту, проверьте нет ли опечаток в Url адресе"));
        }
    }

    private void sendCustomScreenshot(String chatId, String url, Dimension dimension) {
        try {
            File file = webScreenshoter.takeCustomScreenshot(url, dimension);
            InputFile screenshot = new InputFile(file);
            sendDocument(chatId, screenshot);
        } catch (RuntimeException e) {
            log.warn("Failed to get custom screenshot", e);
            bot.sendMessage(new SendMessage(chatId, "Не удается получить доступ к сайту, проверьте нет ли опечаток в Url адресе"));
        }
    }

    private void sendDocument(String chatId, InputFile screenshot) {
        SendDocument document = new SendDocument();
        document.setChatId(chatId);
        document.setDocument(screenshot);
        bot.sendDocument(document);
    }
}
