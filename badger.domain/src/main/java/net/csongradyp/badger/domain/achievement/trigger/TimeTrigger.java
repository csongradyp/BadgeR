package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.Date;

public class TimeTrigger implements ITrigger<Date> {

    private final LocalTime time;

    public TimeTrigger(final LocalTime time) {
        this.time = time;
    }

    @Override
    public Boolean fire(final Date triggerValue) {
        final LocalTime trigger = new DateTime(triggerValue).toLocalTime();
        return time.isEqual(trigger);
    }

    @Override
    public AchievementType getType() {
        return AchievementType.TIME;
    }

    public LocalTime getTime() {
        return time;
    }
}
