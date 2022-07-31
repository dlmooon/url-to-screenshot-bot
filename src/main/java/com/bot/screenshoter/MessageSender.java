package com.bot.screenshoter;

import com.bot.screenshoter.repository.UsersRepo;
import com.bot.screenshoter.telegram.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Service
public class MessageSender {

    @Autowired
    UsersRepo usersRepo;

    @Lazy
    @Autowired
    Bot bot;

    public void sendAllUsersAboutUpdate() {
        List<User> users = usersRepo.getAllUsers();
        for (User user : users) {
            bot.sendMessage(new SendMessage(user.getId().toString(), "An update is out! Please restart the bot (call the /start method)\n\nThe bot is under active development, please share your feedback -> @listener69"));
        }
    }
}
