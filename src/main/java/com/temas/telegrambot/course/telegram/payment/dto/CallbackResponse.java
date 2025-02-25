package com.temas.telegrambot.course.telegram.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by azhdanov on 25.02.2025.
 */
@Data
@AllArgsConstructor
public class CallbackResponse {
    @JsonProperty("orderReference")
    private String orderReference;

    @JsonProperty("status")
    private String status;

    @JsonProperty("time")
    private long time;

    @JsonProperty("signature")
    private String signature;
}
