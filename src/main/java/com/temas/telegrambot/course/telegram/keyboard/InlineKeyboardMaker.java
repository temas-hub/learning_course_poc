package com.temas.telegrambot.course.telegram.keyboard;

import com.temas.telegrambot.course.telegram.content.ButtonNameEnum;
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

    public InlineKeyboardMarkup getInlineVideoButtons(VideoContent videoContent) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(videoContent.getTitle());
        button.setUrl(videoContent.getUrl());

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

    public InlineKeyboardMarkup getInlineBuyButtons(String url) {
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText(ButtonNameEnum.WAY4PAY.getButtonName());
        button1.setUrl(url);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText(ButtonNameEnum.CHECK_PAYMENT.getButtonName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(List.of(button1, button2)));
        return inlineKeyboardMarkup;
    }
}
