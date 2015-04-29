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
    private Map<AchievementType, IAchievement> children;

    public CompositeAchievementBean(final String id, final Relation relation) {
        this.id = id;
        this.relation = relation;
        children = new HashMap<>();
    }

    public Map<AchievementType, IAchievement> getChildren() {
        return children;
    }

    public void addWrappedElement(final AchievementType type, final IAchievement achievement) {
        children.put(type, achievement);
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
        children.values().forEach(achievement -> events.addAll(achievement.getEvent()));
        return events;
    }

    @Override
    public String getTitleKey() {
        return children.values().iterator().next().getTitleKey();
    }

    @Override
    public String getTextKey() {
        return children.values().iterator().next().getTextKey();
    }

    @Override
    public Integer getMaxLevel() {
        Integer maxLevel = 1;
        if (children.containsKey(AchievementType.COUNTER)) {
            maxLevel = children.get(AchievementType.COUNTER).getMaxLevel();
        }
        return maxLevel;
    }

    @Override
    public AchievementType getType() {
        return AchievementType.COMPOSITE;
    }

    public Boolean evaluate(AchievementController controller) {
        return relation.evaluate(controller);
    }

}
