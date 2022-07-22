package com.bot.screenshoter.telegram.facade.handlers;

import com.bot.screenshoter.cache.BotStateCache;
import com.bot.screenshoter.cache.RequestDimensionCache;
import com.bot.screenshoter.cache.RequestUrlCache;
import com.bot.screenshoter.constants.BotStateEnum;
import com.bot.screenshoter.constants.EmojiEnum;
import com.bot.screenshoter.constants.ReplyButtonNameEnum;
import com.bot.screenshoter.keyboards.InlineKeyboardMaker;
import org.openqa.selenium.Dimension;
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
    BotStateCache stateCache;

    @Autowired
    RequestUrlCache urlCache;

    @Autowired
    RequestDimensionCache dimensionCache;

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
        String chatId = message.getChatId().toString();

        switch (button) {
            case TAKE_SCREENSHOT_BUTTON:
                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_URL);
                return new SendMessage(chatId, "Введите URL сайта");

            case ABOUT_BUTTON:
                return new SendMessage(chatId, EmojiEnum.CROWN.get() + " Бот умеет делать скришноты любого веб-сайта без сжатия");

            default:
                return new SendMessage(chatId, "Что-то пошло не так, попробуйте еще раз");
        }
    }

    private BotApiMethod<?> processMessageFromUser(Message message) {
        String chatId = message.getChatId().toString();
        BotStateEnum botState = stateCache.getUsersBotState(chatId);

        switch (botState) {
            case ASK_URL:
                urlCache.addRequestUrl(chatId, message.getText());

                stateCache.setUsersBotState(chatId, BotStateEnum.ASK_TYPE_SCREENSHOT);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Выберите тип скриншота:");
                sendMessage.setChatId(chatId);
                sendMessage.setReplyMarkup(inlineKeyboardMaker.getKeyboardForSelectTypeScreenshot());

                return sendMessage;

            case ASK_DIMENSION:
                if (isCorrectFormat(message.getText())) {
                    Dimension dimension = getDimension(message.getText());
                    if (!isCorrectDimension(dimension)) {
                        return new SendMessage(chatId, "Высота и ширина не могут быть меньше 1 и больше 6000!");
                    }
                    dimensionCache.addRequestDimension(chatId, dimension);

                    stateCache.setUsersBotState(chatId, BotStateEnum.CONFIRM_ACTION);
                    SendMessage message1 = new SendMessage(chatId, "Разрешение: " + dimension.getWidth() + " x " + dimension.getHeight() + "\n" + "Подтвердить действие?");
                    message1.setReplyMarkup(inlineKeyboardMaker.getKeyboardForConfirmOrCancel());
                    return message1;
                } else {
                    return new SendMessage(chatId, "Введите разрешение в верном формате!");
                }

            default:
                return new SendMessage(chatId, "Я вас не понимаю");
        }
    }

    private boolean isCorrectFormat(String text) {
        return text.matches("\\b\\d{2,4}\\h[xXхХ]\\h\\d{2,4}\\b");
    }

    private boolean isCorrectDimension(Dimension dimension) {
        int min = 1;
        int max = 6000;
        return dimension.getWidth() >= min &&
                dimension.getHeight() >= min &&
                dimension.getWidth() <= max &&
                dimension.getHeight() <= max;
    }

    private Dimension getDimension(String text) {
        String[] mas = text.split("[xXхХ]");
        int width = Integer.parseInt(mas[0].trim());
        int height = Integer.parseInt(mas[1].trim());
        return new Dimension(width, height);
    }
}
