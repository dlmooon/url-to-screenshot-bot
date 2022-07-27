package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.WebScreenshoter;
import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonNameEnum;
import com.bot.screenshoter.repository.UrlHistoryRepo;
import com.bot.screenshoter.telegram.Bot;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
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
    Bot bot;

    @Autowired
    WebScreenshoter webScreenshoter;

    @Autowired
    RequestUrlCache urlCache;

    @Autowired
    RequestDimensionCache dimensionCache;

    @Autowired
    BotStateCache stateRepo;

    @Autowired
    UrlHistoryRepo urlHistoryRepo;

    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        InlineButtonNameEnum button = InlineButtonNameEnum.convert(callbackQuery.getData());
        deleteInlineKeyboard(chatId, callbackQuery.getMessage().getMessageId());
        switch (button) {
            case SIMPLE_SCREENSHOT_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                new Thread(() -> sendSimpleScreenshot(chatId, urlCache.getRequestUrl(chatId))).start();
                return new SendMessage(chatId, "Пожалуйста, подождите...");

            case LONG_SCREENSHOT_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                new Thread(() -> sendLongScreenshot(chatId, urlCache.getRequestUrl(chatId))).start();
                return new SendMessage(chatId, "Пожалуйста, подождите...");

            case CUSTOM_SCREENSHOT_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.ASK_DIMENSION);
                return new SendMessage(chatId, "Введите разрешение скриншота, ширина и высота не должны быть больше 6000\n" +
                        "(пример: 1920 x 800)");

            case CONFIRM_BUTTON:
                stateRepo.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                new Thread(() -> sendCustomScreenshot(chatId, urlCache.getRequestUrl(chatId), dimensionCache.getRequestDimension(chatId))).start();
                return new SendMessage(chatId, "Пожалуйста, подождите...");

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
        log.info("Trying to get a simple screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeSimpleScreenshot(url);
            urlHistoryRepo.addUrl(url, "simple", Long.parseLong(chatId));
            boolean isSent = sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            bot.sendMessage(new SendMessage(chatId, "Кажется, что-то пошло не так, попробуйте перезагрузить бота (вызовите команду /start)"));
        } catch (RuntimeException e) {
            log.warn("Failed to get simple screenshot", e);
            bot.sendMessage(new SendMessage(chatId, "Не удается получить доступ к сайту, проверьте нет ли опечаток в Url адресе"));
        }
    }

    private void sendLongScreenshot(String chatId, String url) {
        log.info("Trying to get a long screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeLongScreenshot(url);
            urlHistoryRepo.addUrl(url, "long", Long.parseLong(chatId));
            boolean isSent = sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            bot.sendMessage(new SendMessage(chatId, "Кажется, что-то пошло не так, попробуйте перезагрузить бота (вызовите команду /start)"));
        } catch (RuntimeException e) {
            log.warn("Failed to get long screenshot", e);
            bot.sendMessage(new SendMessage(chatId, "Не удается получить доступ к сайту, проверьте нет ли опечаток в Url адресе"));
        }
    }

    private void sendCustomScreenshot(String chatId, String url, Dimension dimension) {
        log.info("Trying to get a custom screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeCustomScreenshot(url, dimension);
            urlHistoryRepo.addUrl(url, "custom", Long.parseLong(chatId));
            boolean isSent = sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            bot.sendMessage(new SendMessage(chatId, "Кажется, что-то пошло не так, попробуйте перезагрузить бота (вызовите команду /start)"));
        } catch (RuntimeException e) {
            log.warn("Failed to get custom screenshot", e);
            bot.sendMessage(new SendMessage(chatId, "Не удается получить доступ к сайту, проверьте нет ли опечаток в Url адресе"));
        }
    }

    private boolean sendDocument(String chatId, InputFile screenshot) {
        SendDocument document = new SendDocument();
        document.setChatId(chatId);
        document.setDocument(screenshot);
        return bot.sendScreenshotAsDocument(document);
    }
}
