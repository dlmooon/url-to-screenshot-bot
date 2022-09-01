package com.bot.screenshoter.services.templates.impl;

import com.bot.screenshoter.services.templates.ScreenshotTemplate;
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
    public Dimension dimension() {
        return new Dimension(1920, 1080);
    }

    @Override
    public String name() {
        return "simple-screenshot";
    }

    @Override
    public int pageLoadTimeout() {
        return 0;
    }

    @Override
    public ShootingStrategy shootingStrategy() {
        return ShootingStrategies.simple();
    }
}