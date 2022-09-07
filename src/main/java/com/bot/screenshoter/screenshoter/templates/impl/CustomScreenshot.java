package com.bot.screenshoter.screenshoter.templates.impl;

import com.bot.screenshoter.constants.ScreenshotTypeEnum;
import com.bot.screenshoter.screenshoter.templates.ScreenshotTemplate;
import org.openqa.selenium.Dimension;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public class CustomScreenshot implements ScreenshotTemplate {

    private final String url;
    private final Dimension dimension;

    public CustomScreenshot(String url, Dimension dimension) {
        this.url = url;
        this.dimension = dimension;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String name() {
        return "custom-screenshot";
    }

    @Override
    public Dimension dimension() {
        return dimension;
    }

    @Override
    public ScreenshotTypeEnum type() {
        return ScreenshotTypeEnum.CUSTOM;
    }

    @Override
    public ShootingStrategy shootingStrategy() {
        return ShootingStrategies.simple();
    }
}
