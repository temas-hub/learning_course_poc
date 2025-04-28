package com.temas.telegrambot.course.telegram.handlers;

import com.temas.telegrambot.course.telegram.content.*;
import com.temas.telegrambot.course.telegram.data.UserMapper;
import com.temas.telegrambot.course.telegram.keyboard.InlineKeyboardMaker;
import com.temas.telegrambot.course.telegram.keyboard.ReplyKeyboardMaker;
import com.temas.telegrambot.course.telegram.payment.PaymentProviderService;
import com.temas.telegrambot.course.telegram.service.ContentService;
import com.temas.telegrambot.course.telegram.service.RegistrationService;
import com.temas.telegrambot.course.telegram.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
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
    PaymentProviderService paymentProviderService;
    UserService userService;
    RegistrationService registrationService;
    ContentService contentService;

    public BotApiMethod<?> answerMessage(Message message, SpringWebhookBot bot) throws TelegramApiException {
        String chatId = message.getChatId().toString();

        String inputText = message.getText();
        User user = message.getFrom();

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (inputText.equals("/start")) {
            return getStartMessage(user, chatId);
        } else if (inputText.equals(ButtonNameEnum.BUY.getButtonName())) {
            return getBuyResponse(user, chatId);
        } else if (inputText.equals(ButtonNameEnum.TEST.getButtonName())) {
            return getRequestTestMessage(user, chatId);
        } else if (inputText.equals(ButtonNameEnum.CHECK_PAYMENT.getButtonName())) {
            return buildCheckPaymentResponse(user, chatId);
        } else if (inputText.equals(ButtonNameEnum.DAY1.getButtonName())) {
            return getDayMessage(chatId, 1);
        } else if (inputText.equals(ButtonNameEnum.NEXT.getButtonName()) || inputText.equals(ButtonNameEnum.BONUS.getButtonName())) {
            return moveDayMessage(user, chatId, true);
        } else if (inputText.equals(ButtonNameEnum.PREV.getButtonName())) {
            return moveDayMessage(user, chatId, false);
        } else {
            return handleCustomMessage(bot, user, chatId, inputText);
        }
    }

    private SendMessage getRequestTestMessage(User telegramUser, String chatId) {
        var u = userService.getUser(telegramUser.getId()).orElse(
                userService.saveUser(UserMapper.mapUser(telegramUser, chatId)));
        return moveDayMessage(telegramUser, chatId, true);
    }


    private SendMessage getStartMessage(User telegramUser, String chatId) {
        return registrationService.getPayedUser(telegramUser.getId()).map(u ->
          getDayMessage(chatId, u.getDay())
        ).orElseGet(() -> {
            SendMessage sendMessage = new SendMessage(chatId, BotMessages.HELP_MESSAGE.getMessage());
            sendMessage.enableMarkdown(true);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getNoAccessMainMenuKeyboard());
            return sendMessage;
        });
    }

    private SendMessage moveDayMessage(User telegramUser, String chatId, boolean isForward) {
        return userService.getUser(telegramUser.getId()).map(u -> {
            var day = isForward ? contentService.nextDay(u): contentService.prevDay(u);
            if (day != u.getDay()) {
                u.setDay(day);
                userService.saveUser(u);
            }
            return getDayMessage(chatId, day);
        }).orElseThrow();
    }

    private SendMessage getDayMessage(String chatId, int day) {
        var content = contentService.getDayContent(day);
        SendMessage sendMessage = new SendMessage(chatId, content);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getDayXMenu(day));
        return sendMessage;
    }

    private BotApiMethod<?> handleCustomMessage(SpringWebhookBot bot, User telegramUser, String chatId, String text) throws TelegramApiException {
        var c = userService.getUser(telegramUser.getId())
                .flatMap(u -> contentService.getContentById(text));

        if (c.isPresent()) {
            return c.get().buildMessage(chatId, bot);
        }

        throw new IllegalStateException("Unknown content id: " + text);
    }

    private BotApiMethod<?> getBuyResponse(User telegramUser, String chatId) {
        try {
            var user = userService.getUser(telegramUser.getId()).orElse(
                    userService.saveUser(UserMapper.mapUser(telegramUser, chatId)));
            var url = paymentProviderService.getPaymentPageUrl(user);
            SendMessage sendMessage = new SendMessage(chatId, BotMessages.BUY_COURSE.getMessage());
            sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineLinkButton(ButtonNameEnum.WAY4PAY.getButtonName(), url));
            return sendMessage;
        } catch (Exception e) {
            return new SendMessage(chatId,
                    BotMessages.EXCEPTION_WHAT_THE_FUCK.getMessage());
        }
    }

    private BotApiMethod<?> buildCheckPaymentResponse(User telegramUser, String chatId) {
        try {
            var user = userService.getUser(telegramUser.getId());
            if (user.isPresent() && paymentProviderService.confirmOrderApproved(user.get().getOrderReference())) {
                SendMessage sendMessage = new SendMessage(chatId, BotMessages.SUCCESS_PAYMENT.getMessage());
                sendMessage.setReplyMarkup(replyKeyboardMaker.getFullAccessMainMenuKeyboard(user.get()));
                return sendMessage;
            } else {
                SendMessage sendMessage = new SendMessage(chatId, BotMessages.NOT_SUCCESS_PAYMENT.getMessage());
                sendMessage.setReplyMarkup(replyKeyboardMaker.getNoAccessMainMenuKeyboard());
                return sendMessage;
            }
        } catch (Exception e) {
            return new SendMessage(chatId,
                    BotMessages.EXCEPTION_WHAT_THE_FUCK.getMessage());
        }
    }


}
