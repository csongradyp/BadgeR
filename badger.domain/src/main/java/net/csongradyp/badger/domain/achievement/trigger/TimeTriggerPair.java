package net.csongradyp.badger.domain.achievement.trigger;

import java.util.Date;

public class TimeTriggerPair {
    private final Date startTrigger;
    private final Date endTrigger;

    public TimeTriggerPair(final Date startTrigger, final Date endTrigger) {
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
