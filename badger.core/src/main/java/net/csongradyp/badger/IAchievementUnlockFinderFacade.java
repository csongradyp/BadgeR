package net.csongradyp.badger;

import java.util.Collection;
import java.util.Optional;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;

public interface IAchievementUnlockFinderFacade {

    Collection<IAchievementUnlockedEvent> findAll();

    Collection<IAchievementUnlockedEvent> findUnlockables(String event);

    Collection<IAchievementUnlockedEvent> findUnlockables(String event, Long currentValue);

    Collection<IAchievementUnlockedEvent> findUnlockables(String event, Collection<String> owners);

    Collection<IAchievementUnlockedEvent> findUnlockables(String event, Long score, Collection<String> owners);

    Optional<IAchievementUnlockedEvent> getUnlockable(IAchievement achievementBean);
}
