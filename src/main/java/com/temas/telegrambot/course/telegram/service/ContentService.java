package com.temas.telegrambot.course.telegram.service;

import com.temas.telegrambot.course.telegram.content.Content;
import com.temas.telegrambot.course.telegram.content.ContentMessages;
import com.temas.telegrambot.course.telegram.content.FileContent;
import com.temas.telegrambot.course.telegram.content.VideoContent;
import com.temas.telegrambot.course.telegram.data.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by azhdanov on 26.02.2025.
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ContentService {
    private final static Map<Integer, String> contentMessageMap =
            Map.of(1, ContentMessages.DAY_1_CONTENT.getMessage(),
                    2, ContentMessages.DAY_2_CONTENT.getMessage(),
                    3, ContentMessages.DAY_3_CONTENT.getMessage(),
                    4, ContentMessages.DAY_4_CONTENT.getMessage(),
                    5, ContentMessages.DAY_5_CONTENT.getMessage(),
                    6, ContentMessages.DAY_6_CONTENT.getMessage(),
                    7, ContentMessages.DAY_7_CONTENT.getMessage(),
                    8, ContentMessages.DAY_8_CONTENT.getMessage());



    private final static Map<Integer, List<Content>> contentMap = Map.of(
            1, List.of(FileContent.CHECK_LIST, VideoContent.DAY_1, VideoContent.DAY_1_2),
            2, List.of(VideoContent.DAY_2_1, VideoContent.DAY_2_2),
            3, List.of(VideoContent.DAY_3_1, VideoContent.DAY_3_2),
            4, List.of(FileContent.PDF_4_1, VideoContent.DAY_4_1),
            5, List.of(VideoContent.DAY_5_1, FileContent.PDF_5_2),
            6, List.of(FileContent.IMAGE_6_1, VideoContent.DAY_6_2),
            7, List.of(VideoContent.DAY_7_2, FileContent.PDF_7_3),
            8, List.of(VideoContent.BONUS_1, VideoContent.BONUS_2, VideoContent.BONUS_3, VideoContent.DAY_1_2)
    );
    
    private final static Map<String, Content> reverseIndex = Map.ofEntries(
            Map.entry(FileContent.CHECK_LIST.getTitle(), FileContent.CHECK_LIST),
            Map.entry(FileContent.PDF_4_1.getTitle(), FileContent.PDF_4_1),
            Map.entry(FileContent.PDF_5_2.getTitle(), FileContent.PDF_5_2),
            Map.entry(FileContent.IMAGE_6_1.getTitle(), FileContent.IMAGE_6_1),
            Map.entry(FileContent.PDF_7_3.getTitle(), FileContent.PDF_7_3),
            Map.entry(VideoContent.DAY_1.getTitle(), VideoContent.DAY_1),
            Map.entry(VideoContent.DAY_1_2.getTitle(), VideoContent.DAY_1_2),
            Map.entry(VideoContent.DAY_2_1.getTitle(), VideoContent.DAY_2_1),
            Map.entry(VideoContent.DAY_2_2.getTitle(), VideoContent.DAY_2_2),
            Map.entry(VideoContent.DAY_3_1.getTitle(), VideoContent.DAY_3_1),
            Map.entry(VideoContent.DAY_3_2.getTitle(), VideoContent.DAY_3_2),
            Map.entry(VideoContent.DAY_4_1.getTitle(), VideoContent.DAY_4_1),
            Map.entry(VideoContent.DAY_5_1.getTitle(), VideoContent.DAY_5_1),
            Map.entry(VideoContent.DAY_6_2.getTitle(), VideoContent.DAY_6_2),
            Map.entry(VideoContent.DAY_7_2.getTitle(), VideoContent.DAY_7_2),
            Map.entry(VideoContent.BONUS_1.getTitle(), VideoContent.BONUS_1),
            Map.entry(VideoContent.BONUS_2.getTitle(), VideoContent.BONUS_2),
            Map.entry(VideoContent.BONUS_3.getTitle(), VideoContent.BONUS_3)
    );


    private static final int FINAL_DAY_NUMBER = 8;

    public String getDayContent(int dayNumber) {
        return contentMessageMap.get(dayNumber);
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

    public List<Content> getDayContentButtons(int day) {
        return contentMap.get(day);
    }

    public Optional<Content> getContentById(String id) {
        return Optional.ofNullable(reverseIndex.get(id));
    }
}
