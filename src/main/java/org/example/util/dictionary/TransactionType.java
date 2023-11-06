package org.example.util.dictionary;

public enum TransactionType {
    DEBIT ("Дебетовая"),
    CREDIT ("Кредитная");

    private final String title;

    TransactionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
