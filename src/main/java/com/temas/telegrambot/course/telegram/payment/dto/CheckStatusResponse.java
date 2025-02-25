package com.temas.telegrambot.course.telegram.payment.dto;

/**
 * Created by azhdanov on 25.02.2025.
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CheckStatusResponse {

    @JsonProperty("reasonCode")
    private int reasonCode;

    @JsonProperty("orderReference")
    private String orderReference;

    @JsonProperty("transactionStatus")
    private String transactionStatus;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("authCode")
    private String authCode;
}

