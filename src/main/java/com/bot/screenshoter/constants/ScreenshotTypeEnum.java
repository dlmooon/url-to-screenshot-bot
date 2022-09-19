package com.bot.screenshoter.constants;

public enum ScreenshotTypeEnum {
    SIMPLE("simple"),
    LONG("long"),
    CUSTOM("custom");

    private final String name;

    ScreenshotTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
