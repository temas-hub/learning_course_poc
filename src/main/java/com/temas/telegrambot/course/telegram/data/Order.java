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
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@RedisHash("Order")
public class Order implements Serializable {
    @Id String orderReference;

    Long userId;
    Long orderDate;
    String amount;
    OrderStatus status;
}
