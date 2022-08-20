package com.bot.screenshoter.handlers.impl;

import com.bot.screenshoter.LocaleMessageService;
import com.bot.screenshoter.TelegramSender;
import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.handlers.Handler;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class ReplyKeyboardMessageHandler implements Handler {

    @Autowired
    private LocaleMessageService localeMessage;
    @Autowired
    private BotStateCache stateCache;
    @Autowired
    private TelegramSender telegramSender;
    @Autowired
    private InlineKeyboardMaker inlineKeyboardMaker;

    @Override
    public boolean supports(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }

        String text = update.getMessage().getText();
        switch (text) {
            case "\uD83D\uDCF7 Сделать скриншот \uD83D\uDCF7":
            case "\uD83D\uDE80 О боте":
            case "Язык \uD83D\uDD01":
            case "\uD83D\uDCF7 Take a screenshot \uD83D\uDCF7":
            case "\uD83D\uDE80 About bot":
            case "Language \uD83D\uDD01":
                return true;

            default:
                return false;
        }
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        String text = message.getText();

        if (text.equals(localeMessage.getById(chatId, "button_take_screenshot"))) {
            stateCache.setUsersBotState(chatId, BotStateEnum.ASK_URL);
            telegramSender.sendMessage(chatId, "enter_url");
        } else if (text.equals(localeMessage.getById(chatId, "button_about"))) {
            stateCache.setUsersBotState(chatId, BotStateEnum.SHOW_ABOUT);
            telegramSender.sendMessage(chatId, "about_bot");
        } else if (text.equals(localeMessage.getById(chatId, "button_language"))) {
            stateCache.setUsersBotState(chatId, BotStateEnum.ASK_LANGUAGE);
            telegramSender.sendMessage(chatId, "choose_lang", inlineKeyboardMaker.getKeyboardForSelectLang());
        }
    }
}
