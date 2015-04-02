package com.noe.badger.bundle.domain.achievement;

import com.noe.badger.AchievementController;
import com.noe.badger.AchievementType;
import com.noe.badger.bundle.domain.IAchievement;
import com.noe.badger.bundle.relation.Relation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositeAchievementBean implements IAchievement {

    private Relation relation;
    private final String id;
    private Map<AchievementType, IAchievement> wrappedAchievements;

    public CompositeAchievementBean(final String id, final Relation relation) {
        this.id = id;
        this.relation = relation;
        wrappedAchievements = new HashMap<>();
    }

    public Map<AchievementType, IAchievement> getWrappedAchievements() {
        return wrappedAchievements;
    }

    public void addWrappedElement(final AchievementType type, final IAchievement achievement) {
        wrappedAchievements.put(type, achievement);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCategory() {
        return "composite";
    }

    @Override
    public List<String> getEvent() {
        List<String> events = new ArrayList<>();
        wrappedAchievements.values().forEach(achievement -> events.addAll(achievement.getEvent()));
        return events;
    }

    @Override
    public String getTitleKey() {
        return wrappedAchievements.values().iterator().next().getTitleKey();
    }

    @Override
    public String getTextKey() {
        return wrappedAchievements.values().iterator().next().getTextKey();
    }

    @Override
    public Integer getMaxLevel() {
        Integer maxLevel = 1;
        if (wrappedAchievements.containsKey(AchievementType.COUNTER)) {
            maxLevel = wrappedAchievements.get(AchievementType.COUNTER).getMaxLevel();
        }
        return maxLevel;
    }

    public Boolean evaluate(AchievementController controller) {
        return relation.evaluate(controller);
    }

}
