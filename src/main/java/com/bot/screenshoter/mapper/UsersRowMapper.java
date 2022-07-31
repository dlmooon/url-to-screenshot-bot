package com.bot.screenshoter.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUserName(rs.getString("username"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setLanguageCode(rs.getString("lang_code"));
        user.setIsBot(rs.getBoolean("is_bot"));
        return user;
    }
}
