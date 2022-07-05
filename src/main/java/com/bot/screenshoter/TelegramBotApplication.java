package com.bot.screenshoter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramBotApplication {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","/app/.chromedriver/bin/chromedriver");
        SpringApplication.run(TelegramBotApplication.class, args);
    }
}
