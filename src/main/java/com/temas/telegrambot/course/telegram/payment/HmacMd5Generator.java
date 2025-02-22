package com.temas.telegrambot.course.telegram.payment;

/**
 * Created by azhdanov on 19.02.2025.
 */
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HmacMd5Generator {

    public static void main(String[] args) throws Exception {
        // Sample input data
        String merchantAccount = "freelance_user_67b5a584473d6";
        String merchantDomainName = "https://www.instagram.com/ira_ravchak";
        String orderReference = "WFP-BTN-10420649-67b6186619c54";
        String orderDate = "1415379863";
        String amount = "200";
        String currency = "UAH";
        List<String> productNames = Arrays.asList("Курс Энергия ON");
        List<String> productCounts = Arrays.asList("1");
        List<String> productPrices = Arrays.asList("200");

        // Your merchant secret key
        String secretKey = "03354047d6cedde54829c5ff0035dbe4f29923f6";

        // Concatenate all parameters with ";" separator
        StringBuilder dataToSign = new StringBuilder();
        dataToSign.append(merchantAccount).append(";")
                .append(merchantDomainName).append(";")
                .append(orderReference).append(";")
                .append(orderDate).append(";")
                .append(amount).append(";")
                .append(currency).append(";");

        // Append product names
        for (String productName : productNames) {
            dataToSign.append(productName).append(";");
        }

        // Append product counts
        for (String productCount : productCounts) {
            dataToSign.append(productCount).append(";");
        }

        // Append product prices
        for (String productPrice : productPrices) {
            dataToSign.append(productPrice).append(";");
        }

        // Remove the last extra semicolon
        if (dataToSign.length() > 0 && dataToSign.charAt(dataToSign.length() - 1) == ';') {
            dataToSign.setLength(dataToSign.length() - 1);
        }

        // Generate HMAC_MD5 signature
        String signature = generateHmacMd5(dataToSign.toString(), secretKey);
        System.out.println("Generated HMAC_MD5 Signature: " + signature);
    }

    public static String generateHmacMd5(String data, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacMD5");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacMD5");
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hmacBytes);
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
}
