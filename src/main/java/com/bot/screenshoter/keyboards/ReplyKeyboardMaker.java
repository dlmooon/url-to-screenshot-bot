package com.bot.screenshoter.keyboards;

import com.bot.screenshoter.services.LocaleMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {

    @Autowired
    LocaleMessageService messageService;

    public ReplyKeyboardMarkup getMainKeyboard(String langTag) {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(messageService.getByTag("button_take_screenshot", langTag)));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(messageService.getByTag("button_about", langTag)));
        row2.add(new KeyboardButton(messageService.getByTag("button_language", langTag)));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setResizeKeyboard(true);

        return replyKeyboardMarkup;
    }
}
