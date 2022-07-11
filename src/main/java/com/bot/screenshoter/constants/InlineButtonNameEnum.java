package com.bot.screenshoter.constants;

public enum InlineButtonNameEnum {

    SIMPLE_SCREENSHOT_BUTTON(EmojiEnum.MONITOR.get() + " Простой скриншот"),
    LONG_SCREENSHOT_BUTTON  (EmojiEnum.TAPE.get()    + " Длинный скриншот"),
    CUSTOM_SCREENSHOT_BUTTON(EmojiEnum.GEAR .get()   + " Кастомный скриншот"),

    CONFIRM_BUTTON(EmojiEnum.CONFIRM.get() + " Подтвердить"),
    CANCEL_BUTTON (EmojiEnum.CANCEL.get()  + " Отменить");

    private final String buttonText;

    InlineButtonNameEnum(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getText() {
        return buttonText;
    }

    public static InlineButtonNameEnum convert(String callbackData) {
        for (InlineButtonNameEnum button : InlineButtonNameEnum.values()) {
            if (button.name().equals(callbackData)) {
                return button;
            }
        }

        throw new IllegalArgumentException();
    }
}
