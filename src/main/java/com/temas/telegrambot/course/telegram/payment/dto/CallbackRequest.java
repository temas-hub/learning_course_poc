package com.temas.telegrambot.course.telegram.payment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by azhdanov on 25.02.2025.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallbackRequest {
    @JsonProperty("merchantAccount")
    private String merchantAccount;

    @JsonProperty("merchantSignature")
    private String merchantSignature;

    @JsonProperty("orderReference")
    private String orderReference;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("authCode")
    private String authCode;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("createdDate")
    private Long createdDate;

    @JsonProperty("processingDate")
    private Long processingDate;

    @JsonProperty("cardPan")
    private String cardPan;

    @JsonProperty("cardType")
    private String cardType;

    @JsonProperty("issuerBankCountry")
    private String issuerBankCountry;

    @JsonProperty("issuerBankName")
    private String issuerBankName;

    @JsonProperty("recToken")
    private String recToken;

    @JsonProperty("transactionStatus")
    private String transactionStatus;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("reasonCode")
    private Integer reasonCode;

    @JsonProperty("fee")
    private Double fee;

    @JsonProperty("paymentSystem")
    private String paymentSystem;

    @JsonProperty("repayUrl")
    private String repayUrl;

    @JsonProperty("acquirerBankName")
    private String acquirerBankName;

    @JsonProperty("clientName")
    private String clientName;
}