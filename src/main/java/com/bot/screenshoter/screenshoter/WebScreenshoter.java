package com.bot.screenshoter.screenshoter;

import com.bot.screenshoter.screenshoter.templates.ScreenshotTemplate;
import com.bot.screenshoter.utils.FileUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

    private static final int DEFAULT_PAGE_LOAD_TIMEOUT = 1000;

    @Autowired
    private FileUtils fileUtils;

    private ChromeDriver driver;

    public synchronized File takeScreenshot(ScreenshotTemplate screenshotTemplate) throws WebDriverException {
        driver.get(screenshotTemplate.url());
        driver.manage().window().setSize(screenshotTemplate.dimension());
        waitPageLoad(screenshotTemplate.pageLoadTimeout());
        Screenshot screenshot = new AShot().shootingStrategy(screenshotTemplate.shootingStrategy()).takeScreenshot(driver);
        return fileUtils.getFileFromBufferedImage(screenshot.getImage(), screenshotTemplate.name());
    }

    @SneakyThrows
    private void waitPageLoad(Integer seconds) {
        if (seconds == 0) {
            Thread.sleep(DEFAULT_PAGE_LOAD_TIMEOUT);
        } else {
            Thread.sleep(seconds * 1000);
        }
    }

    @PostConstruct
    private void prepareAndRunWebDriver() {
        WebDriverManager.chromedriver().setup();

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

        driver = new ChromeDriver(options);

        log.info("Web driver is prepared and running");
    }

    @PreDestroy
    private void endWebDriverSession() {
        driver.close();
        driver.quit();
        log.info("web driver session is end");
    }
}

