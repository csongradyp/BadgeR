package net.csongradyp.badger.domain.achievement;

import net.csongradyp.badger.domain.AbstractAchievementBean;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;

import java.util.List;

public class TimeRangeAchievementBean extends AbstractAchievementBean implements ITriggerableAchievementBean<TimeTriggerPair> {

    private List<TimeTriggerPair> triggers;

    @Override
    public List<TimeTriggerPair> getTrigger() {
        return triggers;
    }

    @Override
    public void setTrigger(final List<TimeTriggerPair> triggers) {
        this.triggers = triggers;
    }

    @Override
    public String toString() {
        return super.toString() + "trigger=" + triggers + '}';
    }

    @Override
    public AchievementType getType() {
        return AchievementType.TIME_RANGE;
    }
}
