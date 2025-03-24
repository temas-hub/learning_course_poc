package com.temas.telegrambot.course.telegram.service;

import com.temas.telegrambot.course.telegram.content.ContentMessages;
import com.temas.telegrambot.course.telegram.content.VideoContent;
import com.temas.telegrambot.course.telegram.data.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by azhdanov on 26.02.2025.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ContentService {
    private final static Map<Integer, String> contentMap =
            Map.of(1, ContentMessages.DAY_1_CONTENT.getMessage(),
                    2, ContentMessages.DAY_2_CONTENT.getMessage(),
                    3, ContentMessages.DAY_3_CONTENT.getMessage());

    private final static Map<Integer, VideoContent> practiceMap =
            Map.of(1, VideoContent.DAY_1);


    private static final int FINAL_DAY_NUMBER = 7;

    public String getDayContent(int dayNumber) {
        return contentMap.get(dayNumber);
    }

    public VideoContent getDayPracticeVideo(int dayNumber) {
        return practiceMap.get(dayNumber);
    }

    /**
     * @return -1 if it is out of range
     */
    public int nextDay(User user) {
        int day = user.getDay();
        if (++day > FINAL_DAY_NUMBER) {
            return user.getDay();
        }
        return day;
    }

    public int prevDay(User user) {
        int day = user.getDay();
        if (--day <= 0) {
            return user.getDay();
        }
        return day;
    }
}
