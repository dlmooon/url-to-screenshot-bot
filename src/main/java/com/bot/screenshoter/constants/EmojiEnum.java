package com.bot.screenshoter.constants;

public enum EmojiEnum {

    RUSSIA  ("\uD83C\uDDF7\uD83C\uDDFA"),
    ENGLAND ("\uD83C\uDDEC\uD83C\uDDE7"),
    CAMERA  ("\uD83D\uDCF7"),
    PERSON  ("\uD83D\uDC64"),
    ROCKET  ("\uD83D\uDE80"),
    MONITOR ("\uD83D\uDDA5"),
    TAPE    ("\uD83C\uDF9E"),
    CROWN   ("\uD83D\uDC51"),
    CAP     ("\uD83E\uDDE2"),
    CANCEL  ("\u274C"),
    CONFIRM ("\u2714\uFE0F"),
    GEAR    ("\u2699");

    private final String emoji;

    EmojiEnum(String emoji) {
        this.emoji = emoji;
    }

    public String get() {
        return emoji;
    }
}
