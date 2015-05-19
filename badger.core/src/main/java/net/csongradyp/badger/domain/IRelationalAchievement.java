package net.csongradyp.badger.domain;

import java.util.Map;

public interface IRelationalAchievement extends IAchievement<String>, IRelation {

    Map<AchievementType, IAchievement> getChildren();

    void addChild(AchievementType type, IAchievement achievement);
}
