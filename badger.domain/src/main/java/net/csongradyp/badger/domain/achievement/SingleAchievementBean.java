package net.csongradyp.badger.domain.achievement;

import java.util.ArrayList;
import java.util.List;
import net.csongradyp.badger.domain.AbstractAchievementBean;
import net.csongradyp.badger.domain.AchievementType;

public class SingleAchievementBean extends AbstractAchievementBean<String> {

    @Override
    public void setTrigger(String[] trigger) {
    }

    @Override
    public List<String> getTrigger() {
        return new ArrayList<>();
    }

    @Override
    public AchievementType getType() {
        return AchievementType.SINGLE;
    }
}
