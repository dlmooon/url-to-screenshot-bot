package com.bot.screenshoter;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramBotApplication {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        SpringApplication.run(TelegramBotApplication.class, args);
    }
}
