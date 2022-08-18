package com.bot.screenshoter.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {

    boolean supports(Update update);

    int priority();

    void handle(Update update);
}
