package com.temas.telegrambot.course.telegram.data;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Created by azhdanov on 27.04.2025.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
@RedisHash("TesterRequest")
public class TesterRequest {
    @Id
    Long id;

    String userName;
    String firstName;
    String lastName;
    String chatId;
}
