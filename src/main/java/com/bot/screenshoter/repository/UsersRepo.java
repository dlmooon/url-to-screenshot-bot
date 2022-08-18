package com.bot.screenshoter.repository;

import com.bot.screenshoter.mapper.UsersRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Repository
public class UsersRepo {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void registerOrUpdateIfExist(User user) {
        jdbcTemplate.update("INSERT INTO users " +
                        "(user_id, username, first_name, last_name, lang_code, is_bot, timestamp_registration) " +
                        "VALUES (?, ?, ?, ?, ?, ?, current_timestamp(3)) " +
                        "ON CONFLICT (user_id) DO UPDATE SET " +
                        "user_id = excluded.user_id," +
                        "first_name = excluded.first_name," +
                        "last_name = excluded.last_name," +
                        "lang_code = excluded.lang_code",
                user.getId(),
                user.getUserName(),
                user.getFirstName(),
                user.getLastName(),
                user.getLanguageCode(),
                user.getIsBot());

    }

    public boolean isUserExist(Long tg_id) {
        String sql = "SELECT count(*) FROM users WHERE user_id = " + tg_id;
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count > 0;
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", new UsersRowMapper());
    }
}
