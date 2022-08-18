package com.bot.screenshoter;

import com.bot.screenshoter.repository.UsersBotLangRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UsersBotLangRepo botLangRepo;

    public String getById(String chatId, String message) {
        Locale locale = Locale.forLanguageTag(botLangRepo.getLang(Long.parseLong(chatId)));
        return messageSource.getMessage(message, null, locale);
    }

    public String getById(String chatId, Object[] args, String message) {
        Locale locale = Locale.forLanguageTag(botLangRepo.getLang(Long.parseLong(chatId)));
        return messageSource.getMessage(message, args, locale);
    }

    public String getByTag(String message, String langTag) {
        return messageSource.getMessage(message, null, Locale.forLanguageTag(langTag));
    }

    public String getByTag(String message, Object[] args, String langTag) {
        return messageSource.getMessage(message, args, Locale.forLanguageTag(langTag));
    }

    public String getDefault(String message) {
        return messageSource.getMessage(message, null, Locale.getDefault());
    }
}
