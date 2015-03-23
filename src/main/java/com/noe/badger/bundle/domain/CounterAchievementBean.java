package com.noe.badger.bundle.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CounterAchievementBean extends AbstractAchievementBean<Long> {

    private List<Long> trigger;

    public CounterAchievementBean() {
        trigger = new ArrayList<>();
    }

    @Override
    public List<Long> getTrigger() {
        return trigger;
    }

    public void setTrigger(final String[] trigger) {
        Long[] triggers = new Long[trigger.length];
        for (int i = 0; i < trigger.length; i++) {
            final Long value = Long.parseLong(trigger[i]);
            triggers[i] = value;
        }
        Collections.addAll(this.trigger, triggers);
    }

    @Override
    public String toString() {
        return super.toString() +
                "trigger=" + trigger +
                '}';
    }
}
