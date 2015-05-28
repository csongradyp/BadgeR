package net.csongradyp.badger.domain.achievement.trigger;

import net.csongradyp.badger.domain.AchievementType;

public class ScoreTriggerPair implements ITrigger<Long> {
    private final Long startTrigger;
    private final Long endTrigger;

    public ScoreTriggerPair(final Long startTrigger, final Long endTrigger) {
        this.startTrigger = startTrigger;
        this.endTrigger = endTrigger;
    }

    @Override
    public Boolean fire(final Long triggerValue) {
        Boolean triggered;
        if (startTrigger < endTrigger) {
            triggered = isInRange(startTrigger, endTrigger, triggerValue);
        } else {
            triggered = !isInRange(endTrigger, startTrigger, triggerValue);
        }
        return triggered;
    }

    private Boolean isInRange(final Long startTrigger, final Long endTrigger, final Long triggerValue) {
        return triggerValue >= startTrigger && triggerValue <= endTrigger;
    }

    @Override
    public AchievementType getType() {
        return AchievementType.SCORE_RANGE;
    }

    public Long getStartTrigger() {
        return startTrigger;
    }

    public Long getEndTrigger() {
        return endTrigger;
    }
}
