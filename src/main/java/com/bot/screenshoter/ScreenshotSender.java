package com.bot.screenshoter;

import com.bot.screenshoter.repository.UrlHistoryRepo;
import com.bot.screenshoter.telegram.Bot;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

@Slf4j
@Service
public class ScreenshotSender {

    @Lazy
    @Autowired
    Bot bot;

    @Autowired
    WebScreenshoter webScreenshoter;

    @Autowired
    UrlHistoryRepo urlHistoryRepo;

    @Autowired
    LocaleMessageService localeMessage;

    public void sendSimpleScreenshot(String chatId, String url) {
        log.info("Trying to get a simple screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeSimpleScreenshot(url);
            bot.sendBotAction(new SendChatAction(chatId, ActionType.UPLOADDOCUMENT.toString()));
            urlHistoryRepo.addUrl(url, "simple", Long.parseLong(chatId));
            boolean isSent = sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            bot.sendMessage(new SendMessage(chatId, localeMessage.getById(chatId, "restart_bot")));
        } catch (RuntimeException e) {
            log.warn("Failed to get simple screenshot", e);
            bot.sendMessage(new SendMessage(chatId, localeMessage.getById(chatId, "site_not_available")));
        }
    }

    public void sendLongScreenshot(String chatId, String url) {
        log.info("Trying to get a long screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeLongScreenshot(url);
            bot.sendBotAction(new SendChatAction(chatId, ActionType.UPLOADDOCUMENT.toString()));
            urlHistoryRepo.addUrl(url, "long", Long.parseLong(chatId));
            boolean isSent = sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            bot.sendMessage(new SendMessage(chatId, localeMessage.getById(chatId, "restart_bot")));
        } catch (RuntimeException e) {
            log.warn("Failed to get long screenshot", e);
            bot.sendMessage(new SendMessage(chatId, localeMessage.getById(chatId, "site_not_available")));
        }
    }

    public void sendCustomScreenshot(String chatId, String url, Dimension dimension) {
        log.info("Trying to get a custom screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeCustomScreenshot(url, dimension);
            bot.sendBotAction(new SendChatAction(chatId, ActionType.UPLOADDOCUMENT.toString()));
            urlHistoryRepo.addUrl(url, "custom", Long.parseLong(chatId));
            boolean isSent = sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            bot.sendMessage(new SendMessage(chatId, localeMessage.getById(chatId, "restart_bot")));
        } catch (RuntimeException e) {
            log.warn("Failed to get custom screenshot", e);
            bot.sendMessage(new SendMessage(chatId, localeMessage.getById(chatId, "site_not_available")));
        }
    }

    private boolean sendDocument(String chatId, InputFile screenshot) {
        SendDocument document = new SendDocument();
        document.setChatId(chatId);
        document.setDocument(screenshot);
        return bot.sendScreenshotAsDocument(document);
    }
}
