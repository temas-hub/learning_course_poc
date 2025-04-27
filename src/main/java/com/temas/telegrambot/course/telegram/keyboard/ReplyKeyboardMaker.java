package com.temas.telegrambot.course.telegram.keyboard;

import com.temas.telegrambot.course.telegram.content.ButtonNameEnum;
import com.temas.telegrambot.course.telegram.content.Content;
import com.temas.telegrambot.course.telegram.content.VideoContent;
import com.temas.telegrambot.course.telegram.data.User;
import com.temas.telegrambot.course.telegram.service.ContentService;
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
    private final ContentService contentService;

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
        row1.add(new KeyboardButton(VideoContent.INTRO.getId()));
        row1.add(new KeyboardButton(ButtonNameEnum.TEST.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.BUY.getButtonName()));
        row2.add(new KeyboardButton(ButtonNameEnum.CHECK_PAYMENT.getButtonName()));

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(List.of(row1, row2));

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

        return replyKeyboardMarkup;
    }


    public ReplyKeyboardMarkup getDayXMenu(int day) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (Content c : contentService.getDayContentButtons(day)) {
            row.add(new KeyboardButton(c.getTitle()));
            if (row.size() == 2) {
                keyboard.add(row);
                row = new KeyboardRow();
            }
        }
        if (day > 1) {
            row.add(new KeyboardButton(ButtonNameEnum.PREV.getButtonName()));
        }
        if (row.size() == 2) {
            keyboard.add(row);
            row = new KeyboardRow();
        }

        row.add(new KeyboardButton(ButtonNameEnum.NEXT.getButtonName()));
        keyboard.add(row);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
