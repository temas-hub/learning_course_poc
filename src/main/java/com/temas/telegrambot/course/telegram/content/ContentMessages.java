package com.temas.telegrambot.course.telegram.content;

/**
 * Created by azhdanov on 16.02.2025.
 */
public enum ContentMessages {
    DAY_1_CONTENT("День 1: Диагностика энергии\n" +
            "\uD83D\uDCA1 Сегодня ты узнаешь, что мешает тебе жить на 100%.\n" +
            "\uD83D\uDCCD Что мы делаем?\n" +
            "\uD83D\uDD0D Диагностика энергетического состояния (чек-лист).\n" +
            "⚡ Анализ утечек энергии (куда уходит твоя сила).\n" +
            "\uD83E\uDDD8\u200D♀ Практика: «Энергетическое сканирование тела».\n" +
            "\uD83D\uDC8E Твоя задача:\n" +
            "1\uFE0F⃣ Пройти чек-лист и определить главные утечки энергии.\n" +
            "2\uFE0F⃣ Сделать практику сканирования (аудио-инструкция ниже).\n" +
            "3\uFE0F⃣ Записать в дневник 3 вещи, которые снижают твою энергию.\n" +
            "✅ Как закончишь – нажми “Дальше”, чтобы перейти ко 2-му дню.\n");




    private final String message;

    ContentMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
