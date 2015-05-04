package com.noe.badger.bundle.domain.achievement;

import com.noe.badger.bundle.domain.AbstractAchievementBean;
import com.noe.badger.exception.MalformedAchievementDefinition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.format.DateTimeFormat;

public class TimeRangeAchievementBean extends AbstractAchievementBean<TimeRangeAchievementBean.TimeTriggerPair> {

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
            this.triggers.add(new TimeTriggerPair(start, end));
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

    public class TimeTriggerPair {
        private final Date startTrigger;
        private final Date endTrigger;

        public TimeTriggerPair(Date startTrigger, Date endTrigger) {
            this.startTrigger = startTrigger;
            this.endTrigger = endTrigger;
        }

        public Date getStartTrigger() {
            return startTrigger;
        }

        public Date getEndTrigger() {
            return endTrigger;
        }
    }
}
