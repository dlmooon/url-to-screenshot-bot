package com.bot.screenshoter.constants;

public enum InlineButtonEnum {

    SIMPLE_SCREENSHOT_BUTTON,
    LONG_SCREENSHOT_BUTTON,
    CUSTOM_SCREENSHOT_BUTTON,

    CONFIRM_BUTTON,
    CANCEL_BUTTON,

    RUSSIA_LANG_BUTTON,
    ENGLISH_LANG_BUTTON;

    public static InlineButtonEnum convert(String callbackData) {
        for (InlineButtonEnum button : InlineButtonEnum.values()) {
            if (button.name().equals(callbackData)) {
                return button;
            }
        }

        throw new IllegalArgumentException();
    }
}
