package com.bot.screenshoter.services;

import com.bot.screenshoter.services.templates.ScreenshotTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;

@Slf4j
@Service
public class ScreenshotService {

    @Autowired
    WebScreenshoter webScreenshoter;

    public InputFile getScreenshot(ScreenshotTemplate screenshotTemplate) {
        log.info("Trying to get a {}", screenshotTemplate.name());
        try {
            File file = webScreenshoter.takeScreenshot(screenshotTemplate);
            log.info("{} got successfully", StringUtils.capitalize(screenshotTemplate.name()));
            return new InputFile(file);
        } catch (WebDriverException e) {
            log.warn("Failed to get screenshot");
            throw e;
        }
    }
}
