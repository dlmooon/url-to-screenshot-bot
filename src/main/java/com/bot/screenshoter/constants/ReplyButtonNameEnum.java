package com.bot.screenshoter.constants;


public enum ReplyButtonNameEnum {

    TAKE_SCREENSHOT_BUTTON (EmojiEnum.CAMERA.get()  + " Сделать скриншот"),
    PROFILE_BUTTON         (EmojiEnum.PERSON.get()  + " Профиль"),
    ABOUT_BUTTON           (EmojiEnum.ROCKET.get()  + " О боте");

    private final String buttonText;

    ReplyButtonNameEnum(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getText() {
        return buttonText;
    }

    public static ReplyButtonNameEnum convert(String str) {
        for (ReplyButtonNameEnum button: ReplyButtonNameEnum.values()) {
            if (button.getText().equals(str)) {
                return button;
            }
        }

        throw new IllegalArgumentException();
    }

    public static boolean isNameOfButton(String str) {
        for (ReplyButtonNameEnum button: ReplyButtonNameEnum.values()) {
            if (button.getText().equals(str)) {
                return true;
            }
        }

        return false;
    }
}
