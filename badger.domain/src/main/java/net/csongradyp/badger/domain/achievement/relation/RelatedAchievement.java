package net.csongradyp.badger.domain.achievement.relation;

import net.csongradyp.badger.IAchievementUnlockFinderFacade;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.domain.IRelation;

public class RelatedAchievement implements IRelation {

    private IAchievement achievement;

    public RelatedAchievement(final IAchievement achievement) {
        this.achievement = achievement;
    }

    @Override
    public Boolean evaluate(final IAchievementUnlockFinderFacade finder) {
        return !finder.getUnlockable(achievement).isPresent();
    }
}
