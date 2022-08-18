package com.bot.screenshoter.cache;

import lombok.NonNull;
import org.openqa.selenium.Dimension;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestDimensionCache {

    private final Map<String, Dimension> dimensionCache = new HashMap<>();

    public void addRequestDimension(@NonNull String chatId, @NonNull Dimension dimension) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        }

        dimensionCache.put(chatId, dimension);
    }

    public Dimension getRequestDimension(@NonNull String chatId) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        }

        Dimension dimension = dimensionCache.get(chatId);
        return dimension == null ? new Dimension(1920, 1080) : dimension;
    }
}
