package com.bot.screenshoter.cache;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestUrlCache {

    private final Map<String, String> urlCache = new HashMap<>();

    public void addRequestUrl(@NonNull String chatId, @NonNull String url) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        } else if (url.isEmpty()) {
            throw new IllegalArgumentException("url is empty!");
        }

        urlCache.put(chatId, url);
    }

    public String getRequestUrl(@NonNull String chatId) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        }

        String url = urlCache.get(chatId);
        return url == null ? "" : url;
    }
}
