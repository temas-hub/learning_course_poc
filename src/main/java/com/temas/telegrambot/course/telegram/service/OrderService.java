package com.temas.telegrambot.course.telegram.service;

import com.temas.telegrambot.course.telegram.data.Order;
import com.temas.telegrambot.course.telegram.data.OrderRepository;
import com.temas.telegrambot.course.telegram.data.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void approveOrder(String reference) {
        Optional<Order> order = getOrder(reference);
        if (order.isPresent()) {
            Order o = order.get();
            orderRepository.save(Order.builder()
                    .orderReference(o.getOrderReference())
                    .orderDate(o.getOrderDate())
                    .amount(o.getAmount())
                    .status(OrderStatus.APPROVED)
            .build());
        }
    }

    public void rejectOrder(String reference) {
        Optional<Order> order = getOrder(reference);
        if (order.isPresent()) {
            Order o = order.get();
            orderRepository.save(Order.builder()
                    .orderReference(o.getOrderReference())
                    .orderDate(o.getOrderDate())
                    .amount(o.getAmount())
                    .status(OrderStatus.REJECTED)
                    .build());
        }
    }

}
