package com.bot.screenshoter.services.actions.impl;

import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonEnum;
import com.bot.screenshoter.constants.ScreenshotTypeEnum;
import com.bot.screenshoter.repository.UrlHistoryRepo;
import com.bot.screenshoter.screenshoter.templates.ScreenshotTemplate;
import com.bot.screenshoter.screenshoter.templates.impl.CustomScreenshot;
import com.bot.screenshoter.screenshoter.templates.impl.LongScreenshot;
import com.bot.screenshoter.screenshoter.templates.impl.SimpleScreenshot;
import com.bot.screenshoter.services.ScreenshotService;
import com.bot.screenshoter.services.TelegramService;
import com.bot.screenshoter.services.actions.InlineKeyboardAction;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Slf4j
@Service
public class SelectScreenshotTypeAction implements InlineKeyboardAction {

    @Autowired
    private RequestUrlCache urlCache;
    @Autowired
    private BotStateCache stateCache;
    @Autowired
    private RequestDimensionCache dimensionCache;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private UrlHistoryRepo urlHistoryRepo;
    @Autowired
    private ScreenshotService screenshotService;

    @Override
    public boolean supports(InlineButtonEnum button) {
        return button.equals(InlineButtonEnum.SIMPLE_SCREENSHOT_BUTTON) ||
                button.equals(InlineButtonEnum.LONG_SCREENSHOT_BUTTON) ||
                button.equals(InlineButtonEnum.SET_UP_CUSTOM_SCREENSHOT_BUTTON) ||
                button.equals(InlineButtonEnum.TAKE_CUSTOM_SCREENSHOT_BUTTON);
    }

    @Override
    public void handle(String chatId, InlineButtonEnum button) {
        stateCache.setUsersBotState(chatId, BotStateEnum.TAKING_SCREENSHOT);
        String url = urlCache.getRequestUrl(chatId);
        Long url_id = null;
        InputFile file = null;

        switch (button) {
            case SIMPLE_SCREENSHOT_BUTTON:
                url_id = saveInfo(chatId, url, ScreenshotTypeEnum.SIMPLE);
                file = getScreenshot(chatId, new SimpleScreenshot(url));
                break;

            case LONG_SCREENSHOT_BUTTON:
                url_id = saveInfo(chatId, url, ScreenshotTypeEnum.LONG);
                file = getScreenshot(chatId, new LongScreenshot(url));
                break;

            case TAKE_CUSTOM_SCREENSHOT_BUTTON:
                telegramService.sendMessage(chatId, "please_wait");
                url_id = saveInfo(chatId, url, ScreenshotTypeEnum.CUSTOM);
                file = getScreenshot(chatId, new CustomScreenshot(url, dimensionCache.getRequestDimension(chatId)));
                break;

            case SET_UP_CUSTOM_SCREENSHOT_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_DIMENSION);
                telegramService.sendMessage(chatId, "enter_resolution");
                return;
        }

        telegramService.sendChatAction(chatId, ActionType.UPLOADDOCUMENT);
        boolean isSent = telegramService.sendDocument(chatId, file);
        if (isSent) {
            stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
            urlHistoryRepo.setIsSent(url_id, true);
        } else {
            stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
        }
    }

    private InputFile getScreenshot(String chatId, ScreenshotTemplate template) {
        try {
            return screenshotService.getScreenshot(template);
        } catch (WebDriverException e) {
            telegramService.sendMessage(chatId, "site_not_available");
            throw e;
        }
    }

    private Long saveInfo(String chatId, String url, ScreenshotTypeEnum type) {
        try {
            Long url_id = urlHistoryRepo.addUrl(url, type.getName(), Long.parseLong(chatId));
            return url_id;
        } catch (DataIntegrityViolationException e) {
            log.warn("User not registered", e);
            telegramService.sendMessage(chatId, "restart_bot");
            throw e;
        }
    }
}
