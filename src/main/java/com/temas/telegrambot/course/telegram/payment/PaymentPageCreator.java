package com.temas.telegrambot.course.telegram.payment;

import com.temas.telegrambot.course.telegram.data.Order;
import com.temas.telegrambot.course.telegram.data.OrderStatus;
import com.temas.telegrambot.course.telegram.data.User;
import com.temas.telegrambot.course.telegram.service.OrderService;
import com.temas.telegrambot.course.telegram.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * Created by azhdanov on 19.02.2025.
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor()
public class PaymentPageCreator {
    @Value("${course.itemDescription}")
    String itemDescription;
    @Value("${course.price}")
    String price;

    final Way4PayClient paymentSender;
    final OrderReferenceGenerator orderReferenceGenerator;
    final OrderService orderService;
    final UserService userService;

    /**
     * @return payment page url
     * **/
    public String getPaymentPageUrl(User user) throws Exception{
        long date = Instant.now().getEpochSecond();
        Order order = prepareOrder(user.getId(), price, date);


        String url = paymentSender.sendPayment(
                order.getOrderReference(),
                String.valueOf(date),
                price,
                List.of(itemDescription), List.of("1"), List.of(price));

        user.setOrderReference(order.getOrderReference());
        userService.saveUser(user);
        return url;
    }

    public Order prepareOrder(Long userId, String amount, long date) {
        var orderReference = orderReferenceGenerator.generateOrderReference();
        return orderService.save(Order.builder()
                .orderReference(orderReference)
                .userId(userId)
                .orderDate(date)
                .amount(amount)
                .status(OrderStatus.PENDING)
                .build()
        );
    }
}
