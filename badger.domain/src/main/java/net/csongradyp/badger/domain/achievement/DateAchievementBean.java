package net.csongradyp.badger.domain.achievement;

import net.csongradyp.badger.domain.AbstractAchievementBean;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.DateTrigger;

import java.util.ArrayList;
import java.util.List;

public class DateAchievementBean extends AbstractAchievementBean implements ITriggerableAchievementBean<DateTrigger> {

    private List<DateTrigger> trigger;

    public DateAchievementBean() {
        trigger = new ArrayList<>();
    }

    @Override
    public List<DateTrigger> getTrigger() {
        return trigger;
    }

    @Override
    public void setTrigger(final List<DateTrigger> trigger) {
        this.trigger = trigger;
    }

    @Override
    public String toString() {
        return super.toString() + "trigger=" + trigger + '}';
    }

    @Override
    public AchievementType getType() {
        return AchievementType.DATE;
    }
}
