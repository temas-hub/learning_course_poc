package com.temas.telegrambot.course.telegram.keyboard;

import com.temas.telegrambot.course.telegram.content.ButtonNameEnum;
import com.temas.telegrambot.course.telegram.data.User;
import com.temas.telegrambot.course.telegram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by azhdanov on 16.02.2025.
 */
@Component
@RequiredArgsConstructor
public class ReplyKeyboardMaker {
    final UserService userService;

    public ReplyKeyboardMarkup buildMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.START.getButtonName()));
        row1.add(new KeyboardButton(ButtonNameEnum.BUY.getButtonName()));

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(List.of(row1));

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getNoAccessMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.BUY.getButtonName()));
        row1.add(new KeyboardButton(ButtonNameEnum.CHECK_PAYMENT.getButtonName()));

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(List.of(row1));

        return replyKeyboardMarkup;
    }


    public ReplyKeyboardMarkup getFullAccessMainMenuKeyboard(User user) {
        KeyboardRow row1 = new KeyboardRow();
        //TODO
        row1.add(new KeyboardButton(ButtonNameEnum.DAY1.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;    }


    public ReplyKeyboardMarkup getDayXMenu(int day) {
        KeyboardRow row1 = new KeyboardRow();
        if (day == 1) {
            row1.add(new KeyboardButton(ButtonNameEnum.CHECK_LIST.getButtonName()));
            row1.add(new KeyboardButton(ButtonNameEnum.PRACTICE_SOSUD.getButtonName()));
        } else {
            row1.add(new KeyboardButton(ButtonNameEnum.PREV.getButtonName()));
        }

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.PRACTICE.getButtonName()));
        row2.add(new KeyboardButton(ButtonNameEnum.NEXT.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
