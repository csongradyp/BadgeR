package net.csongradyp.badger.domain;

import net.csongradyp.badger.IAchievementUnlockFinderFacade;

public interface IRelation {

    Boolean evaluate(IAchievementUnlockFinderFacade controller);

}
