package com.bot.screenshoter;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.NullOutputStream;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.service.DriverService;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class WebScreenshoter {

    private ChromeDriver driver;

    public synchronized File takeSimpleScreenshot(String url) throws RuntimeException {
        log.info("Take a simple screenshot");
        driver.get(url);
        Screenshot screenshot = new AShot().takeScreenshot(driver);
        return getFileFromBufferedImage(screenshot.getImage(), "simple-screenshot");
    }

    public synchronized File takeLongScreenshot(String url) throws RuntimeException {
        log.info("Take a long screenshot");
        driver.get(url);
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1)).takeScreenshot(driver);
        return getFileFromBufferedImage(screenshot.getImage(), "long-screenshot");
    }

    public synchronized File takeCustomScreenshot(String url, Dimension dimension) throws RuntimeException {
        log.info("Take a custom screenshot");
        driver.get(url);
        driver.manage().window().setSize(dimension);
        waitPageLoad();
        Screenshot screenshot = new AShot().takeScreenshot(driver);
        driver.manage().window().setSize(new Dimension(1920, 1080));
        return getFileFromBufferedImage(screenshot.getImage(), "custom-screenshot");
    }

    public synchronized File takeScreenshotWithScaling(String url, float dpr) {
        log.info("take a screenshot with scaling");
        driver.get(url);
        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.scaling(dpr)).takeScreenshot(driver);
        return getFileFromBufferedImage(screenshot.getImage(), "scaling-screenshot");
    }

    public Object executeScript(String script) {
        return driver.executeScript(script);
    }

    @SneakyThrows
    private void waitPageLoad() {
        // TODO: 22.07.2022 for custom screenshot make page load timeout configurable
    }

    private File getFileFromBufferedImage(BufferedImage image, String name) {
        File file = new File(name + ".png");

        try {
            ImageIO.write(image, "PNG", file);
        } catch (IOException e) {
            log.warn("Failed to write file", e);
        }

        return file;
    }

    @PostConstruct
    private void prepareAndRunWebDriver() {
        WebDriverManager.chromedriver().setup();

        DriverService.Builder<ChromeDriverService, ChromeDriverService.Builder> serviceBuilder = new ChromeDriverService.Builder();
        ChromeDriverService driverService = serviceBuilder.build();
        driverService.sendOutputTo(NullOutputStream.NULL_OUTPUT_STREAM);

        ChromeOptions options = new ChromeOptions();

        //Heroku build pack google chrome options
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--remote-debugging-port=9222");
        options.addArguments("disable-gpu");

        //My options
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("window-size=1920,1080");

        driver = new ChromeDriver(driverService, options);

        log.info("Web driver is prepared and running");
    }

    @PreDestroy
    private void endWebDriverSession() {
        driver.quit();
        log.info("web driver session is end");
    }
}
