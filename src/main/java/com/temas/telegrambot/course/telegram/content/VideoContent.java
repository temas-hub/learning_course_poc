package com.temas.telegrambot.course.telegram.content;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.List;

/**
 * Created by azhdanov on 16.02.2025.
 */

public class VideoContent extends Content {
    public static final Content INTRO = new VideoContent("v_0", "Приветствие", "https://youtu.be/jFopmNoQ1kw?si=bOMqpZjTz4m3vBZ_");
    public static final Content DAY_1 = new VideoContent("v_1_1", "Энергетическое сканирование тела", "https://youtu.be/B81Kz3ks4x8?si=HOc71EeCfGTlm9HW");
    public static final Content DAY_1_2 = new VideoContent("v_1_2", "Энергитические сосуды", "https://youtu.be/aFi7BRkFHh0?si=fe6EaItJOolk34Vn");
    public static final Content DAY_2_1 = new VideoContent("v_2_1", "Ограничивающие убеждения", "https://youtu.be/pSjmQ2y-oOc");
    public static final Content DAY_2_2 = new VideoContent("v_2_2", "Практика глубокой очистки", "https://youtu.be/bnOmz0hncNw?si=zPuZVgFogOOs_9qD");
    public static final Content DAY_2_3 = new VideoContent("v_2_3", "Бонус- практика с символами", "https://youtu.be/EqM4C79k3zk");
    public static final Content DAY_3_1 = new VideoContent("v_3_1", "Энергетическое солнце", "https://youtu.be/OBFHp7Aem68?si=Lm8nb695oUkt5PQ7");
    public static final Content DAY_3_2 = new VideoContent("v_3_2", "Работа с телом", "https://youtu.be/EVcR1y8Tg3c?si=zxp03W3ECySNbrld");
    public static final Content DAY_4_1 = new VideoContent("v_4_1", "Сеанс Изобилия", "https://youtu.be/PaH0KCLWPVY?si=ehtirdyqMXm1VWPI");
    public static final Content DAY_5_1 = new VideoContent("v_5_1", "Медитация «Я выбираю себя»", "https://youtu.be/Ybkquu7U-nI?si=7PEQoi0iqEL6n0I1");
    public static final Content DAY_6_2 = new VideoContent("v_6_2", "Якорь Энергии", "https://youtu.be/lpTqW9ZXefc?si=NpQ1CYfB9k3FusM0");
    public static final Content DAY_7_2 = new VideoContent("v_7_2", "Переход на новый уровень", "https://youtu.be/4KWw5C8QAZ8");
    public static final Content BONUS_1 = new VideoContent("v_b_1", "Развитие интуиции", "https://youtu.be/sA65XOOurlY?si=QluGomxpBF2DMz-t");
    public static final Content BONUS_2 = new VideoContent("v_b_2", "Энергия Притяжения", "https://youtu.be/kw99B6QF8u8?si=t2PprwcEmwBt1bGZ");
    public static final Content BONUS_3 = new VideoContent("v_b_3", "Раскрытие внутренней силы", "https://youtu.be/AYM2QQ9hg6w?si=8-y9s2WoZODrsWeE");

    public VideoContent(String id, String title, String link) {
        super(id, title + " \uD83C\uDFA5", link);
    }

    @Override
    public SendMessage buildMessage(String chatId, SpringWebhookBot bot) {
        SendMessage sendMessage = new SendMessage(chatId, this.getTitle());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(getInlineVideoButtons(this));
        return sendMessage;
    }

    public InlineKeyboardMarkup getInlineVideoButtons(VideoContent videoContent) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(videoContent.getTitle());
        button.setUrl(videoContent.getLink());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(List.of(List.of(button)));
        return inlineKeyboardMarkup;
    }
}