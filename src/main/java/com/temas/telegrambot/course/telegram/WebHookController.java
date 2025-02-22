package com.temas.telegrambot.course.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by azhdanov on 16.02.2025.
 */
@RestController
@AllArgsConstructor
public class WebHookController {
    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final EnergyCourseBot energyCourseBot;

    @PostMapping("/")
    public PartialBotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return energyCourseBot.onWebhookUpdateReceived(update);
    }
}
