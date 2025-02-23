package com.temas.telegrambot.course.telegram.service;

import com.temas.telegrambot.course.telegram.data.OrderStatus;
import com.temas.telegrambot.course.telegram.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by azhdanov on 22.02.2025.
 */
@Component
@RequiredArgsConstructor
public class RegistrationService {

    final OrderService orderService;
    final UserService userService;

    public Optional<User> isPayedUser(Long userId) {
        return userService.getUser(userId)
                .filter(user ->
                    orderService.getOrder(user.getOrderReference())
                    .map(order ->  order.getStatus() == OrderStatus.APPROVED)
                    .orElse(false));
    }

}
