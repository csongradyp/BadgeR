package net.csongradyp.badger.domain;

import java.util.Map;

public interface IRelationalAchievement extends IAchievement<String>, IRelation {

    Map<AchievementType, IAchievement> getChildren();

    void addWrappedElement(AchievementType type, IAchievement achievement);
}
