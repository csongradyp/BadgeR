package com.noe.badger.bundle.relation;

import com.noe.badger.AchievementController;
import com.noe.badger.bundle.domain.IAchievementBean;

public class RelatedAchievement implements IRelation {

    private IAchievementBean achievement;
    private Long currentValue;

    public RelatedAchievement(final IAchievementBean achievement) {
        this.achievement = achievement;
        currentValue = Long.MIN_VALUE;
    }

    public RelatedAchievement(final IAchievementBean achievement, final Long currentValue) {
        this.achievement = achievement;
        this.currentValue = currentValue;
    }

    @Override
    public Boolean evaluate(final AchievementController controller) {
        return !controller.unlockable(currentValue, achievement).isPresent();
    }
}
