package com.bot.screenshoter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

@Repository
public class UrlHistoryRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Long addUrl(String url, String type, Long chatId) {
        String sql = "INSERT INTO url_history " +
                "(url, type, is_sent, timestamp_request, user_id) " +
                "VALUES (?, ?, false, current_timestamp(3), ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"url_id"});
            ps.setString(1, url);
            ps.setString(2, type);
            ps.setLong(3, chatId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void setIsSent(Long url_id, boolean isSent) {
        jdbcTemplate.update("UPDATE url_history SET is_sent = ? WHERE url_id = ?", isSent, url_id);
    }
}
