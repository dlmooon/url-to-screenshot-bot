package com.bot.screenshoter.services.templates;

import org.openqa.selenium.Dimension;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public interface ScreenshotTemplate {

    String url();

    Dimension dimension();

    String name();

    int pageLoadTimeout();

    ShootingStrategy shootingStrategy();
}
