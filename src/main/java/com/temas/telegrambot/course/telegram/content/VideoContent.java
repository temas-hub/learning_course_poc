package com.temas.telegrambot.course.telegram.content;

import lombok.Getter;

/**
 * Created by azhdanov on 16.02.2025.
 */
public enum VideoContent {
    DAY_1("Практика 1","https://www.youtube.com/watch?v=B81Kz3ks4x8"),
    DAY_1_2("Практика с энергитическими сосудами","https://youtu.be/aFi7BRkFHh0?si=F9dzrxpfxIQAUBHc");
    @Getter
    private final String title;
    @Getter
    private final String url;

    VideoContent(String title, String url) {
        this.title = title;
        this.url = url;
    }

}
