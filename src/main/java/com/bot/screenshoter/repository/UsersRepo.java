package com.bot.screenshoter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.User;

@Repository
public class UsersRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void register(User user) {
        if (!isUserExist(user.getId())) {
            jdbcTemplate.update("INSERT INTO users " +
                            "(tg_id, username, first_name, last_name, lang_code, is_bot, timestamp_registration) " +
                            "VALUES(?, ?, ?, ?, ?, ?, current_timestamp(3))",
                    user.getId(),
                    user.getUserName(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getLanguageCode(),
                    user.getIsBot());
        }
    }

    public boolean isUserExist(Long tg_id) {
        String sql = "SELECT count(*) FROM users WHERE tg_id = " + tg_id;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count > 0;
    }
}
