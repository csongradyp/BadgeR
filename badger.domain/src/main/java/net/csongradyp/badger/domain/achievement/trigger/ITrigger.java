package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;

public interface ITrigger<VALUE_TYPE> {

    Boolean fire(VALUE_TYPE triggerValue);

    AchievementType getType();

}
