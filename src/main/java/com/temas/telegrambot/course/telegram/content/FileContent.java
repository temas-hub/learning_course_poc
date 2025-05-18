package com.temas.telegrambot.course.telegram.content;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.io.File;

/**
 * Created by azhdanov on 27.04.2025.
 */
public class FileContent extends Content {
    public static final Content CHECK_LIST = new FileContent("f_1_1", "Чек лист", "1_1.check_list.pdf");
    public static final Content PDF_4_1 = new FileContent("f_4_1", "Глубокая проработка Изобилия", "4_1.pdf");
    public static final Content PDF_5_2 = new FileContent("f_5_2", "Зеркальная трансформация", "5_2.pdf");
    public static final Content IMAGE_6_1 = new FileContent("f_6_1", "Ключи енергии", "6_1.jpg");
    public static final Content PDF_7_3 = new FileContent("f_7_3", "План", "7_3.pdf");

    public FileContent(String id, String title, String link) {
        super(id, title + " \uD83D\uDCC4", link);
    }

    @Override
    public SendMessage buildMessage(String chatId, SpringWebhookBot bot) throws TelegramApiException {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);

        File file = new File(this.getLink());
        sendDocument.setDocument(new InputFile(file));
        sendDocument.setCaption(this.getTitle());
        bot.execute(sendDocument);
        return null;
    }
}