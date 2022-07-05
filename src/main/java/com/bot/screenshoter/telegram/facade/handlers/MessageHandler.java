package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.EmojiEnum;
import com.bot.screenshoter.constants.ReplyButtonNameEnum;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import com.bot.screenshoter.repositories.BotStateRepo;
import com.bot.screenshoter.repositories.RequestUrlCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class MessageHandler {

    @Autowired
    InlineKeyboardMaker inlineKeyboardMaker;

    @Autowired
    BotStateRepo botStateRepo;

    @Autowired
    RequestUrlCache urlCache;

    public BotApiMethod<?> processMessage(Message message) {
        String inputText = message.getText();

        if (ReplyButtonNameEnum.isNameOfButton(inputText)) {
            return processMessageFromReplyKeyboard(message);
        } else {
            return processMessageFromUser(message);
        }
    }

    private BotApiMethod<?> processMessageFromReplyKeyboard(Message message) {
        ReplyButtonNameEnum button = ReplyButtonNameEnum.convert(message.getText());
        String chatID = message.getChatId().toString();

        switch (button) {
            case TAKE_SCREENSHOT_BUTTON:
                botStateRepo.setUsersBotState(chatID, BotStateEnum.ASK_URL);
                return new SendMessage(chatID, "Введите URL сайта");

            case ABOUT_BUTTON:
                return new SendMessage(chatID, EmojiEnum.CROWN.get() + " Бот умеет делать скришноты любого веб-сайта без сжатия");

            default:
                return new SendMessage(chatID, "Что-то пошло не так, попробуйте еще раз");
        }
    }

    private BotApiMethod<?> processMessageFromUser(Message message) {
        BotStateEnum botState = botStateRepo.getUsersBotState(message.getChatId().toString());
        String chatID = message.getChatId().toString();

        switch (botState) {
            case ASK_URL:
                urlCache.addRequestUrl(chatID, message.getText());

                botStateRepo.setUsersBotState(chatID, BotStateEnum.ASK_TYPE_SCREENSHOT);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Выберите тип скриншота:");
                sendMessage.setChatId(chatID);
                sendMessage.setReplyMarkup(inlineKeyboardMaker.getKeyboardForSelectTypeScreenshot());

                return sendMessage;

            default:
                return new SendMessage(chatID, "Я вас не понимаю");
        }
    }
}
