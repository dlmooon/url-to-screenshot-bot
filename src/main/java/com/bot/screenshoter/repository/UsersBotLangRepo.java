package com.bot.screenshoter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsersBotLangRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void setLang(Long chatId, String langTag) {
        jdbcTemplate.update("INSERT INTO users_bot_lang VALUES (?, ?, current_timestamp(3)) " +
                "ON CONFLICT (user_id) DO UPDATE SET " +
                "user_id = excluded.user_id, " +
                "lang_code = excluded.lang_code, " +
                "timestamp_set = excluded.timestamp_set", chatId, langTag);
    }

    public String getLang(Long chatId) {
        String sql = "SELECT lang_code FROM users_bot_lang WHERE user_id = " + chatId;
        try {
            String lang = jdbcTemplate.queryForObject(sql, String.class);
            return lang;
        } catch (EmptyResultDataAccessException e) {
            return "en";
        }
    }
}
