package com.temas.telegrambot.course.telegram.data;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * Created by azhdanov on 20.02.2025.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
@RedisHash("User")
public class User implements Serializable {

    @Id Long id;

    String userName;
    String firstName;
    String lastName;
    String chatId;

    @Setter
    String orderReference;

    @Setter
    int day;
}
