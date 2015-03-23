package com.noe.badger.bundle.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeAchievementBean extends AbstractAchievementBean<String> {

    public static final String PATTERN = "hh:mm";
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
}
