package net.csongradyp.badger.provider.unlock;

import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;

import java.util.Optional;

public interface IUnlockedProvider<TYPE extends IAchievement> {

    Optional<IAchievementUnlockedEvent> getUnlockable(TYPE achievement, Long score);
}
