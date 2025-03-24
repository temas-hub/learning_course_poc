package com.temas.telegrambot.course.telegram.handlers;

import com.temas.telegrambot.course.telegram.content.BotMessages;
import com.temas.telegrambot.course.telegram.content.ButtonNameEnum;
import com.temas.telegrambot.course.telegram.content.ContentMessages;
import com.temas.telegrambot.course.telegram.content.VideoContent;
import com.temas.telegrambot.course.telegram.data.UserMapper;
import com.temas.telegrambot.course.telegram.keyboard.InlineKeyboardMaker;
import com.temas.telegrambot.course.telegram.keyboard.ReplyKeyboardMaker;
import com.temas.telegrambot.course.telegram.payment.PaymentProviderService;
import com.temas.telegrambot.course.telegram.service.ContentService;
import com.temas.telegrambot.course.telegram.service.OrderService;
import com.temas.telegrambot.course.telegram.service.RegistrationService;
import com.temas.telegrambot.course.telegram.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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

    private final OrderService orderService;

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
        } else if (inputText.equals(ButtonNameEnum.CHECK_PAYMENT.getButtonName())) {
            return buildCheckPaymentResponse(user, chatId);
        } else if (inputText.equals(ButtonNameEnum.DAY1.getButtonName())) {
            return getDay1Message(chatId);
        } else if (inputText.equals(ButtonNameEnum.NEXT.getButtonName())) {
            return getDayXMessage(user, chatId, true);
        } else if (inputText.equals(ButtonNameEnum.PREV.getButtonName())) {
            return getDayXMessage(user, chatId, false);
        } else if (inputText.equals(ButtonNameEnum.PRACTICE.getButtonName())) {
            return getPracticeMessage(user, chatId);
        } else if (inputText.equals(ButtonNameEnum.CHECK_LIST.getButtonName())) {
            sendFile(chatId, bot);
            return null;
        } else {
            return new SendMessage(chatId, BotMessages.TBD.getMessage());
        }
    }


    private SendMessage getStartMessage(User telegtramUser, String chatId) {
        var markup = registrationService.isPayedUser(telegtramUser.getId())
                .map(replyKeyboardMaker::getFullAccessMainMenuKeyboard)
                .orElse(replyKeyboardMaker.getNoAccessMainMenuKeyboard());

        SendMessage sendMessage = new SendMessage(chatId, BotMessages.HELP_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    private SendMessage getDay1Message(String chatId) {
        SendMessage sendMessage = new SendMessage(chatId, ContentMessages.DAY_1_CONTENT.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getDay1Menu());
        return sendMessage;
    }

    private SendMessage getDayXMessage(User telegramUser, String chatId, boolean isForward) {
        return userService.getUser(telegramUser.getId()).map(u -> {
            var day = isForward ? contentService.nextDay(u): contentService.prevDay(u);
            if (day != u.getDay()) {
                u.setDay(day);
                userService.saveUser(u);
            }
            var content = contentService.getDayContent(day);
            SendMessage sendMessage = new SendMessage(chatId, content);
            sendMessage.enableMarkdown(true);
            sendMessage.setReplyMarkup(replyKeyboardMaker.getDayXMenu());
            return sendMessage;
        }).orElseThrow();
    }

    private SendMessage getPracticeMessage(User telegramUser, String chatId) {
        return userService.getUser(telegramUser.getId()).map(u -> {
            var day = u.getDay();
            VideoContent videoContent = contentService.getDayPracticeVideo(day);
            SendMessage sendMessage = new SendMessage(chatId, videoContent.getTitle());
            sendMessage.enableMarkdown(true);
            sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineVideoButtons(videoContent));
            return sendMessage;
        }).orElseThrow();
    }

    private SendDocument sendFile(String chatId, SpringWebhookBot bot) throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);

        File file = new File("check_list.pdf");
        sendDocument.setDocument(new InputFile(file));
        sendDocument.setCaption("Here is your file!");
        bot.execute(sendDocument);
        return sendDocument;
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
