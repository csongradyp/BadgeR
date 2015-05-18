package net.csongradyp.badger;

import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;

import java.util.Collection;
import java.util.Optional;

public interface IAchievementUnlockFinderFacade {

    Collection<IAchievementUnlockedEvent> findAll();

    Collection<IAchievementUnlockedEvent> findUnlockables(String event);

    Collection<IAchievementUnlockedEvent> findUnlockables(String event, Long currentValue);

    Collection<IAchievementUnlockedEvent> findUnlockables(String event, Collection<String> owners);

    Optional<IAchievementUnlockedEvent> getUnlockable(IAchievement achievementBean);
}
