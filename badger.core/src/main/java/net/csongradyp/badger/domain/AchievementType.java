package net.csongradyp.badger.domain;

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
            if(achievementType.getType().toLowerCase().equals(type.toLowerCase())) {
                return achievementType;
            }
        }
       throw new IllegalArgumentException("Invalid achievement type: " + type);
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}
