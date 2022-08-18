package com.bot.screenshoter.handlers.impl;

import com.bot.screenshoter.MessageSender;
import com.bot.screenshoter.ScreenshotSender;
import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonEnum;
import com.bot.screenshoter.handlers.Handler;
import com.bot.screenshoter.keyboards.ReplyKeyboardMaker;
import com.bot.screenshoter.repository.UsersBotLangRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
public class CallbackQueryHandler implements Handler {

    @Autowired
    private RequestUrlCache urlCache;
    @Autowired
    private RequestDimensionCache dimensionCache;
    @Autowired
    private BotStateCache stateCache;
    @Autowired
    private ScreenshotSender screenshotSender;
    @Autowired
    private UsersBotLangRepo botLangRepo;
    @Autowired
    private ReplyKeyboardMaker keyboardMaker;
    @Autowired
    private MessageSender messageSender;

    @Override
    public boolean supports(Update update) {
        return update.hasCallbackQuery();
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void handle(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String chatId = callbackQuery.getMessage().getChatId().toString();
        InlineButtonEnum button = InlineButtonEnum.valueOf(callbackQuery.getData());
        messageSender.delete(chatId, callbackQuery.getMessage().getMessageId());
        switch (button) {
            case SIMPLE_SCREENSHOT_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                screenshotSender.sendSimpleScreenshot(chatId, urlCache.getRequestUrl(chatId));
                break;

            case LONG_SCREENSHOT_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                screenshotSender.sendLongScreenshot(chatId, urlCache.getRequestUrl(chatId));
                break;

            case CUSTOM_SCREENSHOT_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_DIMENSION);
                messageSender.send(chatId, "enter_resolution");
                break;

            case CONFIRM_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                screenshotSender.sendCustomScreenshot(chatId, urlCache.getRequestUrl(chatId), dimensionCache.getRequestDimension(chatId));
                break;

            case CANCEL_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                messageSender.send(chatId, "action_canceled");
                break;

            case RUSSIA_LANG_BUTTON:
                botLangRepo.setLang(Long.parseLong(chatId), "ru");
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                messageSender.send(chatId, "language_set", keyboardMaker.getMainKeyboard("ru"));
                break;

            case ENGLISH_LANG_BUTTON:
                botLangRepo.setLang(Long.parseLong(chatId), "en");
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                messageSender.send(chatId, "language_set", keyboardMaker.getMainKeyboard("en"));
                break;

            default:
                messageSender.send(chatId, "something_wrong");
        }
    }
}
