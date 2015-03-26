package com.noe.badger.event.message;

import com.noe.badger.AchievementType;

public class IncrementEvent {

    private final String id;

    public IncrementEvent(String id) {
        this.id = id;
    }

    public AchievementType getType() {
        return AchievementType.COUNTER;
    }

    public String getId() {
        return id;
    }

}
