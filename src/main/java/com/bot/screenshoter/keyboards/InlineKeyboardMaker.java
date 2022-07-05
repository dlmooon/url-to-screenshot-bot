package com.bot.screenshoter.keyboards;

import com.bot.screenshoter.constants.InlineButtonNameEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardMaker {

    public InlineKeyboardMarkup getKeyboardForSelectTypeScreenshot() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(getButton(InlineButtonNameEnum.SIMPLE_SCREENSHOT_BUTTON.getText(), InlineButtonNameEnum.SIMPLE_SCREENSHOT_BUTTON.name()));
        keyboard.add(getButton(InlineButtonNameEnum.LONG_SCREENSHOT_BUTTON.getText(), InlineButtonNameEnum.LONG_SCREENSHOT_BUTTON.name()));
        //keyboard.add(getButton(InlineButtonNameEnum.CUSTOM_SCREENSHOT_BUTTON.getText(), InlineButtonNameEnum.CUSTOM_SCREENSHOT_BUTTON.name()));

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
