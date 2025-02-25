package com.temas.telegrambot.course.telegram.data;

/**
 * Created by azhdanov on 22.02.2025.
 */
public class UserMapper {

    public static User mapUser(org.telegram.telegrambots.meta.api.objects.User user, String chatId) {
        return User.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .chatId(chatId)
                .build();
    }
}
