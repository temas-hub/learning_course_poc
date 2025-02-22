package com.temas.telegrambot.course.telegram.payment;

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

    /**
     * @return payment page url
     * **/
    public String getPaymentPageUrl() throws Exception{
        String date = Long.toString(Instant.now().getEpochSecond());
        return paymentSender.sendPayment(date, price, List.of(itemDescription), List.of("1"), List.of(price));
    }
}
