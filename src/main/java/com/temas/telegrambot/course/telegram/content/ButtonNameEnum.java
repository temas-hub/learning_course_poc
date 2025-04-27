package com.temas.telegrambot.course.telegram.content;

/**
 * Created by azhdanov on 16.02.2025.
 *
 * Старт -> Содержание -> День 1
 *
 */
public enum ButtonNameEnum {
    START("Старт"),
    TEST("Хочу протестировать"),
    BUY("Купить"),
    CHECK_PAYMENT("Проверить оплату"),
    MENU("Содержание"),
    DAY1("День 1"),
    NEXT("Дальше"),
    PREV("Назад"),
    WAY4PAY("WayForPay");


    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
