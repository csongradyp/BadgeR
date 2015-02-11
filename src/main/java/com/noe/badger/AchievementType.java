package com.noe.badger;

public enum AchievementType {
    
    DATE("date"),
    TIME("time"),
    COUNTER("counter"), DATE_COUNTER("date-counter"), TIMED_COUNTER("timed-counter"),
    SINGLE("event");

    private final String type;

    AchievementType(final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
