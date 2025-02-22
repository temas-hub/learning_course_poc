package com.temas.telegrambot.course.telegram.payment;

/**
 * Created by azhdanov on 19.02.2025.
 */
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderReferenceGenerator {



    public String generateOrderReference() {
        long currentNumber = System.currentTimeMillis();
        return String.format("LC-%14d", currentNumber); // Format to "LC-XXXXXXXXXXXXXX"
    }
}