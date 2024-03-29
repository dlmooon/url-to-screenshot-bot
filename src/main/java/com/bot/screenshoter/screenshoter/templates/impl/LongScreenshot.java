package com.bot.screenshoter.screenshoter.templates.impl;

import com.bot.screenshoter.constants.ScreenshotTypeEnum;
import com.bot.screenshoter.screenshoter.templates.ScreenshotTemplate;
import org.openqa.selenium.Dimension;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public class LongScreenshot implements ScreenshotTemplate {

    private final String url;

    public LongScreenshot(String url) {
        this.url = url;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String name() {
        return "long-screenshot";
    }

    @Override
    public Dimension dimension() {
        return new Dimension(1920, 1080);
    }

    @Override
    public ScreenshotTypeEnum type() {
        return ScreenshotTypeEnum.LONG;
    }

    @Override
    public ShootingStrategy shootingStrategy() {
        return ShootingStrategies.viewportPasting(10);
    }
}
