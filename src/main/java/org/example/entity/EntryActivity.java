package org.example.entity;

import org.example.util.dictionary.ActionType;
import java.time.LocalDateTime;

public class EntryActivity {

    LocalDateTime actionTime;
    ActionType actionType;

    public EntryActivity(LocalDateTime actionTime, ActionType actionType) {
        this.actionTime = actionTime;
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "Действие - " + actionType.getTitle() + "; Время - " + actionTime;
    }
}
