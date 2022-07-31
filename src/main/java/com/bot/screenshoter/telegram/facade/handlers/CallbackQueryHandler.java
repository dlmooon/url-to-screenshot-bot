package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.LocaleMessageService;
import com.bot.screenshoter.ScreenshotSender;
import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonEnum;
import com.bot.screenshoter.keyboards.ReplyKeyboardMaker;
import com.bot.screenshoter.repository.UsersBotLangRepo;
import com.bot.screenshoter.telegram.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Slf4j
@Component
public class CallbackQueryHandler {

    @Lazy
    @Autowired
    Bot bot;

    @Autowired
    RequestUrlCache urlCache;

    @Autowired
    RequestDimensionCache dimensionCache;

    @Autowired
    BotStateCache stateCache;

    @Autowired
    ScreenshotSender screenshotSender;

    @Autowired
    UsersBotLangRepo botLangRepo;

    @Autowired
    LocaleMessageService localeMessage;

    @Autowired
    ReplyKeyboardMaker keyboardMaker;

    public BotApiMethod<?> processCallback(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        InlineButtonEnum button = InlineButtonEnum.convert(callbackQuery.getData());
        deleteInlineKeyboard(chatId, callbackQuery.getMessage().getMessageId());
        switch (button) {
            case SIMPLE_SCREENSHOT_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                new Thread(() -> screenshotSender.sendSimpleScreenshot(chatId, urlCache.getRequestUrl(chatId))).start();
                return null;

            case LONG_SCREENSHOT_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                new Thread(() -> screenshotSender.sendLongScreenshot(chatId, urlCache.getRequestUrl(chatId))).start();
                return null;

            case CUSTOM_SCREENSHOT_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_DIMENSION);
                return new SendMessage(chatId, localeMessage.getById(chatId, "enter_resolution"));

            case CONFIRM_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_SCREENSHOT);
                new Thread(() -> screenshotSender.sendCustomScreenshot(chatId, urlCache.getRequestUrl(chatId), dimensionCache.getRequestDimension(chatId))).start();
                return null;

            case CANCEL_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                return new SendMessage(chatId, localeMessage.getById(chatId, "action_canceled"));

            case RUSSIA_LANG_BUTTON:
                botLangRepo.setLang(Long.parseLong(chatId), "ru");
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                SendMessage message = new SendMessage(chatId, localeMessage.getById(chatId, new String[]{callbackQuery.getFrom().getFirstName()}, "greeting"));
                message.setReplyMarkup(keyboardMaker.getMainKeyboard("ru"));
                return message;

            case ENGLISH_LANG_BUTTON:
                botLangRepo.setLang(Long.parseLong(chatId), "en");
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                SendMessage message1 = new SendMessage(chatId, localeMessage.getById(chatId, new String[]{callbackQuery.getFrom().getFirstName()}, "greeting"));
                message1.setReplyMarkup(keyboardMaker.getMainKeyboard("en"));
                return message1;

            default:
                return new SendMessage(chatId, localeMessage.getById(chatId, "something_wrong"));
        }
    }

    private void deleteInlineKeyboard(String chatId, Integer messageID) {
        DeleteMessage message = new DeleteMessage(chatId, messageID);
        bot.deleteMessage(message);
    }
}
