package com.temas.telegrambot.course.telegram.keyboard;

import com.temas.telegrambot.course.telegram.content.VideoContent;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

/**
 * Created by azhdanov on 16.02.2025.
 */
@Component
public class InlineKeyboardMaker {

    public InlineKeyboardMarkup getInlineVideoButtons() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(VideoContent.DAY_1.getTitle());
        button.setUrl(VideoContent.DAY_1.getUrl());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(List.of(button)));
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getInlineLinkButton(String name, String url) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(name);
        button.setUrl(url);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(List.of(button)));
        return inlineKeyboardMarkup;
    }
}
