package net.csongradyp.badger.parser.json.domain;

import java.util.List;

public class AchievementDefinitionJson {

    private List<String> events;
    private AchievementsJson achievements;

    public AchievementDefinitionJson() {
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public AchievementsJson getAchievements() {
        return achievements;
    }

    public void setAchievements(AchievementsJson achievements) {
        this.achievements = achievements;
    }
}
