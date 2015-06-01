package net.csongradyp.badger.domain;

import java.util.List;

public interface IAchievement {

    String getId();

    String getCategory();

    List<String> getSubscriptions();

    String getTitleKey();

    String getTextKey();

    Integer getMaxLevel();

    AchievementType getType();

}
