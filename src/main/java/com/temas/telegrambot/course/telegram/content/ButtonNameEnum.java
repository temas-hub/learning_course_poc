package com.temas.telegrambot.course.telegram.content;

/**
 * Created by azhdanov on 16.02.2025.
 *
 * Старт -> Содержание -> День 1
 *
 */
public enum ButtonNameEnum {
    START("Старт"),
    BUY("Купить"),
    MENU("Содержание"),
    DAY1("День 1"),
    PRESENTATION("Презентация"),
    CHECK_LIST("Чек лист"),
    PRACTICE("\uD83C\uDFA7 Практика: [Видео]"),
    NEXT("Дальше"),
    WAY4PAY("WayForPay");


    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
