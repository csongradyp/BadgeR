package com.noe.badger;

public enum AchievementType {

    DATE("date"),
    TIME("time"),
    COUNTER("counter"), DATE_COUNTER("dateCounter"), TIMED_COUNTER("timedCounter"),
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
