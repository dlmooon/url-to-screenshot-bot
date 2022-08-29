package com.bot.screenshoter.services.actions.impl;

import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.InlineButtonEnum;
import com.bot.screenshoter.keyboards.ReplyKeyboardMaker;
import com.bot.screenshoter.repository.UsersBotLangRepo;
import com.bot.screenshoter.services.TelegramService;
import com.bot.screenshoter.services.actions.InlineKeyboardAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SelectLangAction implements InlineKeyboardAction {

    @Autowired
    private UsersBotLangRepo botLangRepo;
    @Autowired
    private ReplyKeyboardMaker keyboardMaker;
    @Autowired
    private TelegramService telegramService;
    @Autowired
    private BotStateCache stateCache;

    @Override
    public void handle(String chatId, InlineButtonEnum button) {
        switch (button) {
            case RUSSIA_LANG_BUTTON:
                botLangRepo.setLang(Long.parseLong(chatId), "ru");
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                telegramService.sendMessage(chatId, "language_set", keyboardMaker.getMainKeyboard("ru"));
                break;

            case ENGLISH_LANG_BUTTON:
                botLangRepo.setLang(Long.parseLong(chatId), "en");
                stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_MENU);
                telegramService.sendMessage(chatId, "language_set", keyboardMaker.getMainKeyboard("en"));
                break;
        }
    }
}
