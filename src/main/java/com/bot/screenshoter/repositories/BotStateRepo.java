package com.bot.screenshoter.repositories;

import com.bot.screenshoter.constants.BotStateEnum;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BotStateRepo {

    private final Map<String, BotStateEnum> botStateCash = new HashMap<>();

    public void setUsersBotState(String chatID, BotStateEnum botState) {
        botStateCash.put(chatID, botState);
    }

    public BotStateEnum getUsersBotState(String chatID) {
        BotStateEnum botState = botStateCash.get(chatID);
        return botState == null? BotStateEnum.SHOW_MENU : botState;
    }
}
