package com.bot.screenshoter.screenshoter.templates;

import com.bot.screenshoter.constants.ScreenshotTypeEnum;
import org.openqa.selenium.Dimension;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public interface ScreenshotTemplate {

    String url();

    String name();

    Dimension dimension();

    ScreenshotTypeEnum type();

    ShootingStrategy shootingStrategy();
}
