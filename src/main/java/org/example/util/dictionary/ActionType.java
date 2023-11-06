package org.example.util.dictionary;

public enum ActionType {
    CREATED ("Зарегистрирован"),
    ENTRY ("Авторизация"),
    EXIT ("Выход");

    private final String title;

    ActionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
