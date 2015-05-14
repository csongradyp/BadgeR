package net.csongradyp.badger.domain.achievement;

import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.AbstractAchievementBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeAchievementBean extends AbstractAchievementBean<String> {

    private List<String> trigger;

    public TimeAchievementBean() {
        trigger = new ArrayList<>();
    }

    @Override
    public List<String> getTrigger() {
        return trigger;
    }

    public void setTrigger(final String[] trigger) {
        Collections.addAll(this.trigger, trigger);
    }

    @Override
    public String toString() {
        return super.toString() +
                "trigger=" + trigger +
                '}';
    }

    @Override
    public AchievementType getType() {
        return AchievementType.TIME;
    }
}
