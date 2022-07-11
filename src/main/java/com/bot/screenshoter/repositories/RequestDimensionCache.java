package com.bot.screenshoter.repositories;

import org.openqa.selenium.Dimension;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RequestDimensionCache {

    private final Map<String, Dimension> dimensionCache = new HashMap<>();

    public void addRequestDimension(String chatID, Dimension dimension) {
        dimensionCache.put(chatID, dimension);
    }

    public Dimension getRequestDimension(String chatID) {
        return dimensionCache.get(chatID);
    }
}
