package net.csongradyp.badger.domain.achievement.trigger;

import java.util.Date;
import net.csongradyp.badger.domain.AchievementType;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public class TimeTriggerPair implements ITrigger<Date> {
    private final LocalTime startTrigger;
    private final LocalTime endTrigger;

    public TimeTriggerPair(final LocalTime startTrigger, final LocalTime endTrigger) {
        this.startTrigger = startTrigger;
        this.endTrigger = endTrigger;
    }

    @Override
    public Boolean fire(final Date triggerValue) {
        Boolean triggered;
        final LocalTime time = new DateTime(triggerValue).toLocalTime();
        if (startTrigger.isBefore(endTrigger)) {
            triggered = isWithinStartAndEndTime(time);
        } else {
            triggered = !isWithinStartAndEndTime(time);
        }
        return triggered;
    }

    private Boolean isWithinStartAndEndTime(LocalTime time) {
        return (time.isAfter(startTrigger) || time.isEqual(startTrigger)) && (time.isBefore(endTrigger) || time.isEqual(endTrigger));
    }

    @Override
    public AchievementType getType() {
        return AchievementType.TIME_RANGE;
    }

    public LocalTime getStartTrigger() {
        return startTrigger;
    }

    public LocalTime getEndTrigger() {
        return endTrigger;
    }
}
