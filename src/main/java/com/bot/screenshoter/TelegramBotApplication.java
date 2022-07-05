package com.bot.screenshoter;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramBotApplication {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Desktop\\chromedriver_win32\\chromedriver.exe");
        SpringApplication.run(TelegramBotApplication.class, args);
    }
}
