package com.temas.telegrambot.course.telegram.config;

import com.temas.telegrambot.course.telegram.EnergyCourseBot;
import com.temas.telegrambot.course.telegram.handlers.CallbackQueryHandler;
import com.temas.telegrambot.course.telegram.handlers.MessageHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

/**
 * Created by azhdanov on 16.02.2025.
 */
@Configuration
@AllArgsConstructor
public class SpringConfig {
    private final TelegramConfig telegramConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookURL()).build();
    }

    @Bean
    public EnergyCourseBot springWebhookBot(SetWebhook setWebhook,
                                            MessageHandler messageHandler,
                                            CallbackQueryHandler callbackQueryHandler) {
        EnergyCourseBot bot = new EnergyCourseBot(setWebhook, telegramConfig.getBotToken(), messageHandler, callbackQueryHandler);

        bot.setBotPath(telegramConfig.getWebhookURL());
        bot.setBotUsername(telegramConfig.getBotName());

        return bot;
    }
}