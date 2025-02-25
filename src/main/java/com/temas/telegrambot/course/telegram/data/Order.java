package com.temas.telegrambot.course.telegram.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * Created by azhdanov on 20.02.2025.
 */
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
