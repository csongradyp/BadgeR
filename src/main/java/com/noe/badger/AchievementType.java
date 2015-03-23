package com.noe.badger;

public enum AchievementType {
    
    DATE("date"),
    TIME("time"), TIME_RANGE("timeRange"),
    COUNTER("counter"),
    SINGLE("single");

    private final String type;

    AchievementType(final String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
