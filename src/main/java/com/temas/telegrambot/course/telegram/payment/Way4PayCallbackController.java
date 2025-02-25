package com.temas.telegrambot.course.telegram.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.temas.telegrambot.course.telegram.content.BotMessages;
import com.temas.telegrambot.course.telegram.data.Order;
import com.temas.telegrambot.course.telegram.data.OrderStatus;
import com.temas.telegrambot.course.telegram.data.User;
import com.temas.telegrambot.course.telegram.keyboard.ReplyKeyboardMaker;
import com.temas.telegrambot.course.telegram.payment.dto.CallbackRequest;
import com.temas.telegrambot.course.telegram.payment.dto.CallbackResponse;
import com.temas.telegrambot.course.telegram.service.OrderService;
import com.temas.telegrambot.course.telegram.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import static com.temas.telegrambot.course.telegram.payment.Way4PayClient.generateHmac;

/**
 * Created by azhdanov on 19.02.2025.
 */
@RestController
@RequiredArgsConstructor
public class Way4PayCallbackController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OrderService orderService;
    private final UserService userService;
    private final ReplyKeyboardMaker menuKeyboardMaker;
    private final SpringWebhookBot bot;

    @Value("${wayforpay.secretKey}")
    private String secretKey;


    @PostMapping(value = "/callback", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> handleWayForPayCallback(@RequestParam Map<String, String> requestParams) {
        try {
            String jsonPayload = requestParams.keySet().iterator().next();
            CallbackRequest callbackRequest = objectMapper.readValue(jsonPayload, CallbackRequest.class);
            var orderReference = callbackRequest.getOrderReference();
            var transactionStatus = callbackRequest.getTransactionStatus();

            Optional<Order> order;
            if (transactionStatus.equals("Approved")) {
                order = orderService.approveOrder(orderReference);
            } else {
                order = orderService.rejectOrder(orderReference);
            }
            if (order.isPresent()) {
                sendPaymentStatusMessage(
                        order.get().getUserId(),
                        order.get().getStatus().equals(OrderStatus.APPROVED));
            }


            String time = String.valueOf(System.currentTimeMillis());

            StringJoiner joiner = new StringJoiner(";");
            joiner.add(callbackRequest.getOrderReference())
                    .add("accept")
                    .add(time);

            String signature = generateHmac(joiner.toString(), secretKey);

            CallbackResponse response = new CallbackResponse(
                    callbackRequest.getOrderReference(),
                    "accept",
                    System.currentTimeMillis(),
                    signature
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "; request: " + requestParams);
            throw new RuntimeException(e);
        }
    }

    private void sendPaymentStatusMessage(Long userId, boolean success) throws TelegramApiException {
        Optional<User> user = userService.getUser(userId);
        if (user.isPresent()) {
            if (success) {
                SendMessage message = new SendMessage(user.get().getChatId(), BotMessages.SUCCESS_PAYMENT.getMessage());
                message.setReplyMarkup(menuKeyboardMaker.getFullAccessMainMenuKeyboard(user.get()));
                bot.execute(message);
            } else {
                SendMessage message = new SendMessage(user.get().getChatId(), BotMessages.NOT_SUCCESS_PAYMENT.getMessage());
                message.setReplyMarkup(menuKeyboardMaker.getNoAccessMainMenuKeyboard());
                bot.execute(message);
            }
        }
    }
}
