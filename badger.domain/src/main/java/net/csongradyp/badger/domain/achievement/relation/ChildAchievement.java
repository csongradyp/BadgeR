package net.csongradyp.badger.domain.achievement.relation;

import net.csongradyp.badger.IAchievementUnlockFinderFacade;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IRelation;

public class ChildAchievement implements IRelation {

    private IAchievement achievement;

    public ChildAchievement(final IAchievement achievement) {
        this.achievement = achievement;
    }

    @Override
    public Boolean evaluate(final IAchievementUnlockFinderFacade unlockFinder) {
        return unlockFinder.getUnlockable(achievement).isPresent();
    }
}
