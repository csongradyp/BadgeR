package net.csongradyp.badger.domain.achievement;

import net.csongradyp.badger.domain.AbstractAchievementBean;
import net.csongradyp.badger.domain.AchievementType;
import net.csongradyp.badger.domain.ITriggerableAchievementBean;
import net.csongradyp.badger.domain.achievement.trigger.ScoreTriggerPair;

import java.util.List;

public class ScoreRangeAchievementBean extends AbstractAchievementBean implements ITriggerableAchievementBean<ScoreTriggerPair> {

    private List<ScoreTriggerPair> triggers;

    @Override
    public List<ScoreTriggerPair> getTrigger() {
        return triggers;
    }

    @Override
    public void setTrigger(final List<ScoreTriggerPair> triggers) {
        this.triggers = triggers;
    }

    @Override
    public AchievementType getType() {
        return AchievementType.SCORE_RANGE;
    }
}
