package com.bot.screenshoter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UrlHistoryRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void addUrl(String url, String type, Long chatId) {
        jdbcTemplate.update("INSERT INTO url_history " +
                "(url, type, is_sent, timestamp_request, fk_user_id) " +
                "VALUES (?, ?, false, current_timestamp(3), ?)",
                url,
                type,
                chatId);
    }

    public void setIsSent(Long chatId, boolean isSent) {
        jdbcTemplate.update("UPDATE url_history SET is_sent = ? WHERE fk_user_id = ?", isSent, chatId);
    }
}
