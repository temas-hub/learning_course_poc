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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        @Value("${wayforpay.apiVersion}")
    String apiVersion;

    @Value("${wayforpay.currency}")
    String currency;

    @Value("${wayforpay.secretKey}")
    String secretKey;

    @Value("${wayforpay.api.create_payment}")
    String createPaymentUrl;

    final RestTemplate restTemplate = new RestTemplate();

    public String sendPayment(String orderReference, String orderDate, String amount,
                              List<String> productNames, List<String> productCounts, List<String> productPrices) throws Exception {


        // Step 1: Concatenate the parameters for signature
        StringBuilder dataToSign = new StringBuilder();
        dataToSign.append(merchantAccount).append(";")
                .append(merchantDomainName).append(";")
                .append(orderReference).append(";")
                .append(orderDate).append(";")
                .append(amount).append(";")
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept-Charset", "utf-8");

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String url = createPaymentUrl + "?behavior=offline";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        var paymentPageUrl = extractUrlFromResponse(response, "url");

        // Step 5: Return the response
        return paymentPageUrl;
    }

    public String checkPaymentStatus(String orderReference) throws Exception {
        // Step 1: Generate the signature
        String dataToSign = merchantAccount + ";" + orderReference;

        String signature = generateHmacMd5(dataToSign, secretKey);

        // Step 2: Prepare the request payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("transactionType", "CHECK_STATUS");
        requestBody.put("merchantAccount", merchantAccount);
        requestBody.put("orderReference", orderReference);
        requestBody.put("merchantSignature", signature);
        requestBody.put("apiVersion", apiVersion);

        // Step 3: Send the request
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange("https://api.wayforpay.com/api", HttpMethod.POST, requestEntity, String.class);

        var transactionStatus = extractUrlFromResponse(response, "transactionStatus");

        // Step 4: Return the response body
        return transactionStatus;
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

    private String extractUrlFromResponse(ResponseEntity<String> response, String field) {
        String url = null;
        try {
            // Parse the response body
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // Extract the "url" field
            if (rootNode.has(field)) {
                url = rootNode.get(field).asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }
}
