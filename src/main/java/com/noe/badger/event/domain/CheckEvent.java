package com.noe.badger.event.domain;

import com.noe.badger.AchievementType;

public class CheckEvent {

    private final AchievementType type;
    private final String id;
    private String value;

    public CheckEvent( AchievementType type, String id, String value ) {
        this.type = type;
        this.id = id;
        this.value = value;
    }

    public CheckEvent( AchievementType type, String id ) {
        this.type = type;
        this.id = id;
    }

    public AchievementType getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }
}
