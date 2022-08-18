package com.bot.screenshoter.handlers.impl;

import com.bot.screenshoter.handlers.Handler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class UpdateHandler implements Handler {

    @Autowired
    private List<Handler> handlers;

    @Override
    public boolean supports(Update update) {
        return true;
    }

    @Override
    public int priority() {
        return 1;
    }

    @Override
    public void handle(Update update) {
        try {
            handleUpdate(update);
        } catch (RuntimeException e) {
            log.warn("Error handling update", e);
        }
    }

    private void handleUpdate(Update update) {
        handlers.stream()
                .sorted(Comparator.comparingInt(Handler::priority))
                .filter(handler -> handler.supports(update))
                .findFirst()
                .ifPresent(handler -> handler.handle(update));
    }
}
