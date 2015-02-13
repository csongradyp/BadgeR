package com.noe.badger.bundle.domain;

import org.joda.time.format.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;

public class DateAchievementBean extends AbstractAchievementBean<Date> {

    public static final String PATTERN = "yyyy-MM-dd";
    private Date[] trigger;

    @Override
    public Date[] getTrigger() {
        return trigger;
    }

    public void setTrigger( String[] trigger ) {
        Date[] triggers = new Date[trigger.length];
        for (int i = 0; i < trigger.length; i++) {
            final Date value = DateTimeFormat.forPattern(PATTERN).parseDateTime(trigger[i]).toDate();
            triggers[i] = value;
        }
        this.trigger = triggers;
    }

    @Override
    public String toString() {
        return super.toString() +
               "trigger=" + Arrays.toString( trigger ) +
               '}';
    }
}
