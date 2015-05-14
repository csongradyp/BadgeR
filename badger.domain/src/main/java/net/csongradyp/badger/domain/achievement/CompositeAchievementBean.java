package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.csongradyp.badger.IAchievementController;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IRelation;
import net.csongradyp.badger.domain.IRelationalAchievement;
import net.csongradyp.badger.domain.achievement.relation.Relation;

public class CompositeAchievementBean implements IRelationalAchievement {

    private Relation relation;
    private final String id;
    private Map<AchievementType, IAchievement> children;

    public CompositeAchievementBean(final String id, final Relation relation) {
        this.id = id;
        this.relation = relation;
        children = new HashMap<>();
    }

    @Override
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
    public List<IRelation> getTrigger() {
        final List<IRelation> relationAsTriggers = Collections.emptyList();
        relationAsTriggers.add(relation);
        return relationAsTriggers;
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

    @Override
    public Boolean evaluate(IAchievementController controller) {
        return relation.evaluate(controller);
    }

}
