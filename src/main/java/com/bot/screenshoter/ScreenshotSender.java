package com.bot.screenshoter;

import com.bot.screenshoter.repository.UrlHistoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

@Slf4j
@Service
public class ScreenshotSender {

    @Autowired
    WebScreenshoter webScreenshoter;

    @Autowired
    UrlHistoryRepo urlHistoryRepo;

    @Autowired
    TelegramSender telegramSender;

    public void sendSimpleScreenshot(String chatId, String url) {
        log.info("Trying to get a simple screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeSimpleScreenshot(url);
            telegramSender.sendChatAction(chatId, ActionType.UPLOADDOCUMENT);
            urlHistoryRepo.addUrl(url, "simple", Long.parseLong(chatId));
            boolean isSent = telegramSender.sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            telegramSender.sendMessage(chatId, "restart_bot");
        } catch (RuntimeException e) {
            log.warn("Failed to get simple screenshot", e);
            telegramSender.sendMessage(chatId, "site_not_available");
        }
    }

    public void sendLongScreenshot(String chatId, String url) {
        log.info("Trying to get a long screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeLongScreenshot(url);
            telegramSender.sendChatAction(chatId, ActionType.UPLOADDOCUMENT);
            urlHistoryRepo.addUrl(url, "long", Long.parseLong(chatId));
            boolean isSent = telegramSender.sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            telegramSender.sendMessage(chatId, "restart_bot");
        } catch (RuntimeException e) {
            log.warn("Failed to get long screenshot", e);
            telegramSender.sendMessage(chatId, "site_not_available");
        }
    }

    public void sendCustomScreenshot(String chatId, String url, Dimension dimension) {
        log.info("Trying to get a custom screenshot for id - {}", chatId);
        try {
            File file = webScreenshoter.takeCustomScreenshot(url, dimension);
            telegramSender.sendChatAction(chatId, ActionType.UPLOADDOCUMENT);
            urlHistoryRepo.addUrl(url, "custom", Long.parseLong(chatId));
            boolean isSent = telegramSender.sendDocument(chatId, new InputFile(file));
            if (isSent) {
                urlHistoryRepo.setIsSent(Long.parseLong(chatId), true);
            }
        } catch (DataIntegrityViolationException e) {
            log.warn("User with id - {} not registered!", chatId);
            telegramSender.sendMessage(chatId, "restart_bot");
        } catch (RuntimeException e) {
            log.warn("Failed to get custom screenshot", e);
            telegramSender.sendMessage(chatId, "site_not_available");
        }
    }
}
