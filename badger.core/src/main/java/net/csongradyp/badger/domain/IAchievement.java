package net.csongradyp.badger.domain;

import java.util.List;

public interface IAchievement<TRIGGER> {

    String getId();

    String getCategory();

    List<String> getEvent();

    String getTitleKey();

    String getTextKey();

    List<TRIGGER> getTrigger();

    Integer getMaxLevel();

    AchievementType getType();

}
