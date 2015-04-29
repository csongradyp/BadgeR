package com.noe.badger;

public enum AchievementType {
    
    DATE("date"),
    TIME("time"), TIME_RANGE("timeRange"),
    COUNTER("counter"),
    SINGLE("single"),
    COMPOSITE("composite");

    private final String type;

    AchievementType(final String type) {
        this.type = type;
    }

    public static AchievementType parse(final String type) {
        for ( AchievementType achievementType : values() ) {
            if(achievementType.getType().toLowerCase().equals(type)) {
                return achievementType;
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
