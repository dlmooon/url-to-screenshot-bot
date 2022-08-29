package com.bot.screenshoter.utils;

import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Component
public class UrlUtils {

    public boolean isCorrectUrl(String url) {
        try {
            URL checkUrl = new URL(url);
            checkUrl.toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
