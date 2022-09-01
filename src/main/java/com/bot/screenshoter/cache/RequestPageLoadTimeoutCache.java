package com.bot.screenshoter.cache;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestPageLoadTimeoutCache {

    private final Map<String, Integer> pageLoadTimeoutCache = new HashMap<>();

    public void addRequestPageLoadTimeout(@NonNull String chatId, @NonNull Integer time) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        }

        pageLoadTimeoutCache.put(chatId, time);
    }

    public Integer getRequestPageLoadTimeout(@NonNull String chatId) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        }

        Integer time = pageLoadTimeoutCache.get(chatId);
        return time == null ? 0 : time;
    }
}
