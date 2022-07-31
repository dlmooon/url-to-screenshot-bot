package com.bot.screenshoter.keyboards;

import com.bot.screenshoter.LocaleMessageService;
import com.bot.screenshoter.constants.InlineButtonEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardMaker {

    @Autowired
    LocaleMessageService localeMessage;

    public InlineKeyboardMarkup getKeyboardForSelectTypeScreenshot(String chatId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(getButton(localeMessage.getById(chatId, "button_simple_screenshot"), InlineButtonEnum.SIMPLE_SCREENSHOT_BUTTON.name()));
        keyboard.add(getButton(localeMessage.getById(chatId, "button_long_screenshot"), InlineButtonEnum.LONG_SCREENSHOT_BUTTON.name()));
        keyboard.add(getButton(localeMessage.getById(chatId, "button_custom_screenshot"), InlineButtonEnum.CUSTOM_SCREENSHOT_BUTTON.name()));

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getKeyboardForConfirmOrCancel(String chatId) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(getButton(localeMessage.getById(chatId, "button_confirm"), InlineButtonEnum.CONFIRM_BUTTON.name()));
        keyboard.add(getButton(localeMessage.getById(chatId, "button_cancel"), InlineButtonEnum.CANCEL_BUTTON.name()));

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getKeyboardForSelectLang() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(getButton(localeMessage.getByTag("button_ru_lang", "ru"), InlineButtonEnum.RUSSIA_LANG_BUTTON.name()));
        keyboard.add(getButton(localeMessage.getByTag("button_en_lang", "en"), InlineButtonEnum.ENGLISH_LANG_BUTTON.name()));

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);

        return row;
    }
}
