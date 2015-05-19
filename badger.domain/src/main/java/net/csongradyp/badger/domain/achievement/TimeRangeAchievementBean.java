package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.csongradyp.badger.domain.AbstractAchievementBean;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.achievement.trigger.TimeTriggerPair;
import net.csongradyp.badger.exception.MalformedAchievementDefinition;
import org.joda.time.format.DateTimeFormat;

public class TimeRangeAchievementBean extends AbstractAchievementBean<TimeTriggerPair> {

    public static final String PATTERN = "HH:mm";
    private List<TimeTriggerPair> triggers;

    @Override
    public List<TimeTriggerPair> getTrigger() {
        return triggers;
    }

    public void setTrigger(String[] trigger) {
        this.triggers = new ArrayList<>();
        if (trigger.length % 2 != 0) {
            throw new MalformedAchievementDefinition("Time range does not properly set for achievement " + getId() + ". One of the triggers does not have an end time");
        }
        for (int i = 0; i < trigger.length - 1; i = i + 2) {
            final Date start = DateTimeFormat.forPattern(PATTERN).parseDateTime(trigger[i]).toDate();
            final Date end = DateTimeFormat.forPattern(PATTERN).parseDateTime(trigger[i + 1]).toDate();
            final TimeTriggerPair timeTriggerPair = new TimeTriggerPair(start, end);
            this.triggers.add(timeTriggerPair);
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                "trigger=" + triggers +
                '}';
    }

    @Override
    public AchievementType getType() {
        return AchievementType.TIME_RANGE;
    }

}
