package net.csongrady.badger.domain;

import net.csongrady.badger.IAchievementController;

public interface IRelation {

    Boolean evaluate(IAchievementController controller);

}
