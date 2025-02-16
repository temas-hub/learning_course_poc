package com.temas.telegrambot.course.telegram.content;

import lombok.Getter;

/**
 * Created by azhdanov on 16.02.2025.
 */
public enum VideoContent {
    DAY_1("Практика 1","https://www.youtube.com/watch?v=u-pSGPREmZE");
    @Getter
    private final String title;
    @Getter
    private final String url;

    VideoContent(String title, String url) {
        this.title = title;
        this.url = url;
    }

}
