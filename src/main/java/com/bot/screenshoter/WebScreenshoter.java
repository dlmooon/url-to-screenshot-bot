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
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class WebScreenshoter {

    private WebDriver webDriver;

    public synchronized File takeSimpleScreenshot(String url) {
        log.info("Take a simple screenshot");

        if (!isPageExist(url)) {
            return null;
        }

        webDriver.get(url);
        waitPageLoad();
        Screenshot screenshot = new AShot().takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage(), "simple-screenshot");
    }

    public synchronized File takeLongScreenshot(String url) {
        log.info("Take a long screenshot");

        if (isPageExist(url)) {
            return null;
        }

        webDriver.get(url);
        waitPageLoad();
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(10)).takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage(), "long-screenshot");
    }

    public synchronized File takeCustomScreenshot(String url, Dimension dimension) {
        log.info("Take a custom screenshot");

        if (isPageExist(url)) {
            return null;
        }

        webDriver.get(url);
        waitPageLoad();
        webDriver.manage().window().setSize(dimension);
        Screenshot screenshot = new AShot().takeScreenshot(webDriver);
        webDriver.manage().window().setSize(new Dimension(1920, 1080));
        return getFileFromBufferedImage(screenshot.getImage(), "custom-screenshot");
    }

    public synchronized File takeScreenshotWithScaling(String url, float dpr) {
        log.info("take a screenshot with scaling");
        webDriver.get(url);
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.scaling(dpr)).takeScreenshot(webDriver);
        return getFileFromBufferedImage(screenshot.getImage(), "scaling-screenshot");
    }

    private void waitPageLoad() {
//        long PAGE_LOAD_TIMEOUT = 5;
//        webDriver.manage().timeouts().implicitlyWait(PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
    }

    private boolean isPageExist(String url) {
        if (url == null) {
            return false;
        }

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            return false;
        }
    }

    private File getFileFromBufferedImage(BufferedImage image, String name) {
        File file = new File(name + ".png");

        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            log.warn(e.toString());
        }

        return file;
    }

    @PostConstruct
    private void prepareAndRunWebDriver() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("window-size=1920,1080");

        webDriver = new ChromeDriver(options);

        log.info("Web driver is prepared and running");
    }
}
