package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.AbstractAchievementBean;

public class DateAchievementBean extends AbstractAchievementBean<String> {

    private List<String> trigger;

    public DateAchievementBean() {
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
        return AchievementType.DATE;
    }
}
