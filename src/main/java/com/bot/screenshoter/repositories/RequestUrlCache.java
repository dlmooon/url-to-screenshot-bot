package com.bot.screenshoter.repositories;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RequestUrlCache {

    private final Map<String, String> urlCache = new HashMap<>();

    public void addRequestUrl(String chatID, String url) {
        urlCache.put(chatID, url);
    }

    public String getRequestUrl(String chatID) {
        String url = urlCache.get(chatID);
        return url;
    }
}
