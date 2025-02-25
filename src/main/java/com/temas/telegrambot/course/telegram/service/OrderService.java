package com.temas.telegrambot.course.telegram.service;

import com.temas.telegrambot.course.telegram.content.BotMessages;
import com.temas.telegrambot.course.telegram.data.Order;
import com.temas.telegrambot.course.telegram.data.OrderRepository;
import com.temas.telegrambot.course.telegram.data.OrderStatus;
import com.temas.telegrambot.course.telegram.data.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import javax.swing.plaf.BorderUIResource;
import java.util.Optional;

/**
 * Created by azhdanov on 22.02.2025.
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrder(String reference) {
        return orderRepository.findById(reference);
    }

    public Optional<Order> approveOrder(String reference) {
        Optional<Order> order = getOrder(reference);
        if (order.isPresent()) {
            Order o = order.get();
            o.setStatus(OrderStatus.APPROVED);
            orderRepository.save(o);
        }
        return order;
    }

    public Optional<Order> rejectOrder(String reference) {
        Optional<Order> order = getOrder(reference);
        if (order.isPresent()) {
            Order o = order.get();
            o.setStatus(OrderStatus.REJECTED);
            orderRepository.save(o);
        }
        return order;
    }

}
