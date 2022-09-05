package com.bot.screenshoter.screenshoter.templates.impl;

import com.bot.screenshoter.screenshoter.templates.ScreenshotTemplate;
import org.openqa.selenium.Dimension;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public class CustomScreenshot implements ScreenshotTemplate {

    private final String url;
    private final Dimension dimension;
    private final Integer pageLoadTimeout;

    public CustomScreenshot(String url, Dimension dimension, Integer pageLoadTimeout) {
        this.url = url;
        this.dimension = dimension;
        this.pageLoadTimeout = pageLoadTimeout;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public Dimension dimension() {
        return dimension;
    }

    @Override
    public String name() {
        return "custom-screenshot";
    }

    @Override
    public int pageLoadTimeout() {
        return pageLoadTimeout;
    }

    @Override
    public ShootingStrategy shootingStrategy() {
        return ShootingStrategies.simple();
    }
}
