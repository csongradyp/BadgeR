package net.csongradyp.badger.domain.achievement;

import net.csongradyp.badger.domain.AbstractAchievementBean;
import net.csongradyp.badger.domain.AchievementType;

public class SingleAchievementBean extends AbstractAchievementBean {

    @Override
    public AchievementType getType() {
        return AchievementType.SINGLE;
    }
}
