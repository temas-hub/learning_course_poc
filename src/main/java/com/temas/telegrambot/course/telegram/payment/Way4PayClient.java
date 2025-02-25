package com.temas.telegrambot.course.telegram.payment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temas.telegrambot.course.telegram.config.TelegramConfig;
import com.temas.telegrambot.course.telegram.data.Order;
import com.temas.telegrambot.course.telegram.data.OrderStatus;
import com.temas.telegrambot.course.telegram.payment.dto.PaymentRequest;
import com.temas.telegrambot.course.telegram.payment.dto.PaymentResponse;
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
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.temas.telegrambot.course.telegram.payment.HmacMd5Generator.generateHmacMd5;

/**
 * Created by azhdanov on 19.02.2025.
 */
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Way4PayClient {
    private static final String HMAC_MD5_ALGORITHM = "HmacMD5";
    public static final String MERCHANT_AUTH_TYPE = "SimpleSignature";

    final TelegramConfig config;

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

    @Value("${wayforpay.api.callback.url}")
    String callbackUrl;

    final RestTemplate restTemplate = new RestTemplate();

    public PaymentResponse sendPayment(String orderReference, String orderDate, String amount,
                                       List<String> productNames, List<String> productCounts, List<String> productPrices) throws Exception {
        // Step 0: Create request
        String callbackPath = config.getWebhookURL() + callbackUrl;
        PaymentRequest paymentRequest = new PaymentRequest(merchantAccount, MERCHANT_AUTH_TYPE, merchantDomainName,
                orderReference, orderDate, amount, currency, productNames, productCounts, productPrices, callbackPath, null);

        // Step 1: Generate signature
        String signature = generatePaymentSignature(paymentRequest);
        paymentRequest.setMerchantSignature(signature);

        // Step 2: Send request
        String url = createPaymentUrl + "?behavior=offline";
        return sendRequest(url, paymentRequest, PaymentResponse.class);
    }


    /**
     * Generates HMAC signature for payment requests.
     */
    private String generatePaymentSignature(PaymentRequest request) {
        try {
            // Quote product names
            List<String> quotedProductNames = request.getProductName().stream()
                    .map(name -> "\"" + name + "\"")
                    .toList();

            // Concatenate values with ";"
            StringJoiner joiner = new StringJoiner(";");
            joiner.add(request.getMerchantAccount())
                    .add(request.getMerchantDomainName())
                    .add(request.getOrderReference())
                    .add(request.getOrderDate())
                    .add(request.getAmount())
                    .add(request.getCurrency());
            request.getProductName().forEach(joiner::add);
            request.getProductCount().forEach(joiner::add);
            request.getProductPrice().forEach(joiner::add);

            return generateHmac(joiner.toString(), secretKey);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate payment signature", e);
        }
    }

    private <T> T sendRequest(String url,  Object requestBody, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.ACCEPT, "application/json");

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);


        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        try {
            return new ObjectMapper().readValue(response.getBody(), responseType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON response: " + response.getBody(), e);
        }
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

    /**
     * Generates an HMAC-MD5 signature.
     */
    public static String generateHmac(String data, String secretKey) throws Exception {
        try {
            Mac mac = Mac.getInstance(HMAC_MD5_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_MD5_ALGORITHM);
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hmacBytes);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
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
