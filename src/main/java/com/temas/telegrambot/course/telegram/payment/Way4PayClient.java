package com.temas.telegrambot.course.telegram.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temas.telegrambot.course.telegram.data.Order;
import com.temas.telegrambot.course.telegram.data.OrderStatus;
import com.temas.telegrambot.course.telegram.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by azhdanov on 19.02.2025.
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Way4PayClient {
    @Value("${wayforpay.merchantAccount}")
    String merchantAccount;

    @Value("${wayforpay.merchantDomainName}")
    String merchantDomainName;

    @Value("${wayforpay.currency}")
    String currency;

    @Value("${wayforpay.secretKey}")
    String secretKey;

    @Value("${wayforpay.api.create_payment}")
    String createPaymentUrl;



    public String sendPayment(String orderReference, String orderDate, String amount,
                              List<String> productNames, List<String> productCounts, List<String> productPrices) throws Exception {


        // Step 1: Concatenate the parameters for signature
        StringBuilder dataToSign = new StringBuilder();
        dataToSign.append(merchantAccount).append(";")
                .append(merchantDomainName).append(";")
                .append(orderReference).append(";")
                .append(orderDate).append(";")
                .append(amount.toString()).append(";")
                .append(currency).append(";");

        for (String productName : productNames) {
            dataToSign.append(productName).append(";");
        }

        for (String productCount : productCounts) {
            dataToSign.append(productCount).append(";");
        }

        for (String productPrice : productPrices) {
            dataToSign.append(productPrice).append(";");
        }

        // Remove the last extra semicolon
        dataToSign.setLength(dataToSign.length() - 1);

        // Step 2: Generate HMAC_MD5 signature
        String signature = generateHmacMd5(dataToSign.toString(), secretKey);

        List<String> quotedProductNames = productNames.stream()
                .map(name -> "\"" + name + "\"")
                .toList();
        List<String> quotedProductPrices = productPrices.stream()
                .map(name -> "\"" + name + "\"")
                .toList();
        List<String> quotedProductCounts = productCounts.stream()
                .map(name -> "\"" + name + "\"")
                .toList();

        // Step 3: Create the JSON request body
        String requestBody = String.format(
                """
                        {
                          "merchantAccount": "%s",
                          "merchantAuthType": "SimpleSignature",
                          "merchantDomainName": "%s",
                          "orderReference": "%s",
                          "orderDate": "%s",
                          "amount": "%s",
                          "currency": "%s",
                          "productName": %s,
                          "productPrice": %s,
                          "productCount": %s,
                          "merchantSignature": "%s"
                        }""",
                merchantAccount, merchantDomainName, orderReference, orderDate, amount, currency,
                quotedProductNames, quotedProductPrices, quotedProductCounts, signature
        );

        // Step 4: Send the request using RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept-Charset", "utf-8");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String url = createPaymentUrl + "?behavior=offline";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        var paymentPageUrl = extractUrlFromResponse(response);

        // Step 5: Return the response
        return paymentPageUrl;
    }

    private String generateHmacMd5(String data, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacMD5");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacMD5");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hmacBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0'); // Ensure 2-digit hex
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String extractUrlFromResponse(ResponseEntity<String> response) {
        String url = null;
        try {
            // Parse the response body
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Extract the "url" field
            if (rootNode.has("url")) {
                url = rootNode.get("url").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
