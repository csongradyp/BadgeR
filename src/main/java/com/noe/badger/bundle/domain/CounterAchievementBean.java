package com.noe.badger.bundle.domain;

import java.util.Arrays;

public class CounterAchievementBean extends AbstractAchievementBean<Long> {

    private Long[] trigger;

    @Override
    public Long[] getTrigger() {
        return trigger;
    }

    public void setTrigger( String[] trigger ) {
        Long[] triggers = new Long[trigger.length];
        for (int i = 0; i < trigger.length; i++) {
            final Long value = Long.parseLong(trigger[i]);
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
