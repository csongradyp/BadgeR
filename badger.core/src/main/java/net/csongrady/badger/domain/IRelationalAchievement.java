package net.csongrady.badger.domain;

import java.util.Map;

public interface IRelationalAchievement extends IAchievement<IRelation>, IRelation {

    Map<AchievementType, IAchievement> getChildren();

    void addWrappedElement(AchievementType type, IAchievement achievement);
}
