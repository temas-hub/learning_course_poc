package com.temas.telegrambot.course.telegram.payment.dto;

/**
 * Created by azhdanov on 25.02.2025.
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @JsonProperty("merchantAccount")
    private String merchantAccount;

    @JsonProperty("merchantAuthType")
    private String merchantAuthType;

    @JsonProperty("merchantDomainName")
    private String merchantDomainName;

    @JsonProperty("orderReference")
    private String orderReference;

    @JsonProperty("orderDate")
    private String orderDate;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("productName")
    private List<String> productName;

    @JsonProperty("productCount")
    private List<String> productCount;

    @JsonProperty("productPrice")
    private List<String> productPrice;

    @JsonProperty("serviceUrl")
    private String serviceUrl;

    @JsonProperty("merchantSignature")
    private String merchantSignature;
}
