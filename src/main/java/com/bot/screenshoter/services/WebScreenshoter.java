package com.bot.screenshoter.services;

import com.bot.screenshoter.services.templates.ScreenshotTemplate;
import com.bot.screenshoter.utils.FileUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.NullOutputStream;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;

@Slf4j
@Service
public class WebScreenshoter {

    @Autowired
    FileUtils fileUtils;

    private ChromeDriver driver;

    public synchronized File takeScreenshot(ScreenshotTemplate screenshotTemplate) throws WebDriverException {
        driver.get(screenshotTemplate.url());
        driver.manage().window().setSize(screenshotTemplate.dimension());
        waitPageLoad(screenshotTemplate.pageLoadTimeout());
        Screenshot screenshot = new AShot().shootingStrategy(screenshotTemplate.shootingStrategy()).takeScreenshot(driver);
        clearDataAndOpenDefaultPage();
        return fileUtils.getFileFromBufferedImage(screenshot.getImage(), screenshotTemplate.name());
    }

    private void clearDataAndOpenDefaultPage() {
        driver.getSessionStorage().clear();
        driver.getLocalStorage().clear();
        driver.manage().deleteAllCookies();
        driver.get("about:blank");
    }

    @SneakyThrows
    private void waitPageLoad(Integer seconds) {
        if (seconds != 0) {
            Thread.sleep(seconds * 1000);
        }
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

