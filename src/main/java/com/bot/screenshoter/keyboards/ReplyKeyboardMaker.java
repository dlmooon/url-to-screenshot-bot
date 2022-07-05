package com.bot.screenshoter.keyboards;

import com.bot.screenshoter.constants.ReplyButtonNameEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ReplyButtonNameEnum.TAKE_SCREENSHOT_BUTTON.getText()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ReplyButtonNameEnum.ABOUT_BUTTON.getText()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
