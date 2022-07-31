package com.bot.screenshoter.cache;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LocaleCache {

    private final Map<String, String> localeCash = new HashMap<>();

    public void setUsersLocale(@NonNull String chatId, @NonNull String locale) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        } else if (locale.isEmpty()) {
            throw new IllegalArgumentException("locale is empty!");
        }

        localeCash.put(chatId, locale);
    }

    public String getUsersLocale(@NonNull String chatId) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        }

        String locale = localeCash.get(chatId);
        return locale == null ? "en" : locale;
    }
}
