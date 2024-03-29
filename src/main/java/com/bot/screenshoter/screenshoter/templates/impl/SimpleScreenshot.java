package com.bot.screenshoter.screenshoter.templates.impl;

import com.bot.screenshoter.constants.ScreenshotTypeEnum;
import com.bot.screenshoter.screenshoter.templates.ScreenshotTemplate;
import org.openqa.selenium.Dimension;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public class SimpleScreenshot implements ScreenshotTemplate {

    private final String url;

    public SimpleScreenshot(String url) {
        this.url = url;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String name() {
        return "simple-screenshot";
    }

    @Override
    public Dimension dimension() {
        return new Dimension(1920, 1080);
    }

    @Override
    public ScreenshotTypeEnum type() {
        return ScreenshotTypeEnum.SIMPLE;
    }

    @Override
    public ShootingStrategy shootingStrategy() {
        return ShootingStrategies.simple();
    }
}
