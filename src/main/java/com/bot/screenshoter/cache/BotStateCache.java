package com.bot.screenshoter.cache;

import com.bot.screenshoter.constants.BotStateEnum;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotStateCache {

    private final Map<String, BotStateEnum> botStateCash = new HashMap<>();

    public void setUsersBotState(@NonNull String chatId, @NonNull BotStateEnum botState) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        }

        botStateCash.put(chatId, botState);
    }

    public BotStateEnum getUsersBotState(@NonNull String chatId) {
        if (chatId.isEmpty()) {
            throw new IllegalArgumentException("chatId is empty!");
        }

        BotStateEnum botState = botStateCash.get(chatId);
        return botState == null ? BotStateEnum.SHOW_MENU : botState;
    }
}
