package com.temas.telegrambot.course.telegram.content;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

/**
 * Created by azhdanov on 27.04.2025.
 */
public abstract class Content {
    @Getter
    private final String id;
    @Getter
    private final String title;
    @Getter
    private final String link;

    public Content(String id, String title, String link) {
        this.id = id;
        this.title = title;
        this.link = link;
    }

    public abstract SendMessage buildMessage(String chatId, SpringWebhookBot bot) throws TelegramApiException;
}
