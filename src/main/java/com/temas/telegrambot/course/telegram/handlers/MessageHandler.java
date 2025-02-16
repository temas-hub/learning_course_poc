package com.temas.telegrambot.course.telegram.handlers;

import com.temas.telegrambot.course.telegram.content.BotMessages;
import com.temas.telegrambot.course.telegram.content.ButtonNameEnum;
import com.temas.telegrambot.course.telegram.content.ContentMessages;
import com.temas.telegrambot.course.telegram.content.VideoContent;
import com.temas.telegrambot.course.telegram.keyboard.InlineKeyboardMaker;
import com.temas.telegrambot.course.telegram.keyboard.ReplyKeyboardMaker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.File;

/**
 * Created by azhdanov on 16.02.2025.
 */
@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {
    InlineKeyboardMaker inlineKeyboardMaker;
    ReplyKeyboardMaker replyKeyboardMaker;

    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot bot) throws TelegramApiException {
        String chatId = message.getChatId().toString();

        String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
            return getStartMessage(chatId);
        } else if (inputText.equals(ButtonNameEnum.START.getButtonName())) {
            return getDay1Message(chatId);
        } else if (inputText.equals(ButtonNameEnum.PRACTICE.getButtonName())) {
            return getPracticeMessage(chatId);
        } else if (inputText.equals(ButtonNameEnum.CHECK_LIST.getButtonName())) {
            sendFile(chatId, bot);
            return null;
        } else {
            return new SendMessage(chatId, BotMessages.TBD.getMessage());
        }
    }

    private SendMessage getStartMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, BotMessages.HELP_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage getDay1Message(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, ContentMessages.DAY_1_CONTENT.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getDay1Menu());
        return sendMessage;
    }

    private SendMessage getPracticeMessage(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, VideoContent.DAY_1.getTitle());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineVideoButtons());
        return sendMessage;
    }

    private SendDocument sendFile(String chatId, SpringWebhookBot bot) throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);

        File file = new File("README.txt");
        sendDocument.setDocument(new InputFile(file));
        sendDocument.setCaption("Here is your file!");
        bot.execute(sendDocument);
        return sendDocument;
    }


}
