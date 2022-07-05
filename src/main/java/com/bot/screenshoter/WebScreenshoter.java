package com.bot.screenshoter;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class WebScreenshoter {

    private WebDriver webDriver;

    public File takeSimpleScreenshot(String url) {
        log.info("Take a simple screenshot");
        webDriver.manage().window().fullscreen();
        webDriver.get(url);
        Screenshot screenshot = new AShot().takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage());
    }

    public File takeLongScreenshot(String url) {
        log.info("Take a long screenshot");
        webDriver.get(url);
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1)).takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage());
    }

    public File takeCustomScreenshot(String url, Dimension dimension) {
        log.info("Take a custom screenshot");

        webDriver.get(url);
        webDriver.manage().window().setSize(dimension);

        Screenshot screenshot = new AShot().takeScreenshot(webDriver);

        webDriver.manage().window().fullscreen();
        return getFileFromBufferedImage(screenshot.getImage());
    }

    public File takeScreenshotWithScaling(String url, float dpr) {
        log.info("take a screenshot with scaling");
        webDriver.get(url);
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.scaling(dpr)).takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage());
    }

    public File take(String url) {
        return new File("");
    }

    private File getFileFromBufferedImage(BufferedImage image) {
        File file = new File("screenshot.png");

        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    @PostConstruct
    private void prepareWebDriver() {
//        ChromeOptions options = new ChromeOptions();
//
//        options.setBinary("/app/.chromedriver/bin/chromedriver");
//        options.addArguments("--enable-javascript");
//        options.addArguments("--headless");
//        options.addArguments("--disable-gpu");
//        options.addArguments("--no-sandbox");
//
//        webDriver = new ChromeDriver(options);

        webDriver = new ChromeDriver();

        log.debug("Web driver is prepared");
    }
}
