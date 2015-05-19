package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.csongradyp.badger.IAchievementUnlockFinderFacade;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.IAchievement;
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

    public void addChild(final AchievementType type, final IAchievement achievement) {
        children.put(type, achievement);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCategory() {
        final Optional<IAchievement> childAchievement = children.values().stream().filter(child -> child.getCategory() != "default").findAny();
        if(childAchievement.isPresent()) {
            return childAchievement.get().getCategory();
        }
        return "default";
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
    public List<String> getTrigger() {
        List<String> triggers = new ArrayList<>();
        children.values().forEach(achievement -> achievement.getTrigger().forEach(t -> triggers.add(t.toString())));
        return triggers;
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
    public Boolean evaluate(IAchievementUnlockFinderFacade controller) {
        return relation.evaluate(controller);
    }

}
