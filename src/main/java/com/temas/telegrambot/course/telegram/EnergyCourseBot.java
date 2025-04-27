package com.temas.telegrambot.course.telegram;


import com.temas.telegrambot.course.telegram.content.BotMessages;
import com.temas.telegrambot.course.telegram.handlers.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import com.temas.telegrambot.course.telegram.handlers.CallbackQueryHandler;

import java.io.IOException;

/**
 * Created by azhdanov on 16.02.2025.
 */

@Getter
@Setter
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnergyCourseBot extends SpringWebhookBot {
    String botPath;
    String botUsername;

    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    public EnergyCourseBot(SetWebhook setWebhook, String botToken,
                           MessageHandler messageHandler,CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook, botToken);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (Exception e) {
            log.error("Error processing update: {}", e.getMessage(), e);
            return new SendMessage(update.getMessage().getChatId().toString(),
                    BotMessages.EXCEPTION_WHAT_THE_FUCK.getMessage());
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException, TelegramApiException {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            return callbackQueryHandler.processCallbackQuery(callbackQuery);
        } else {
            Message message = update.getMessage();
            if (message != null) {
                return messageHandler.answerMessage(update.getMessage(), this);
            }
        }
        return null;
    }
}
