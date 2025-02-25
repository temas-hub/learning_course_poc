package com.temas.telegrambot.course.telegram.payment.dto;

/**
 * Created by azhdanov on 25.02.2025.
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentResponse {

    @JsonProperty("url")
    private String paymentUrl;
}
