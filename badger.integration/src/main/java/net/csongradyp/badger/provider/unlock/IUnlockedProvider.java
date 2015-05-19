package net.csongradyp.badger.provider.unlock;

import java.util.Optional;
import net.csongradyp.badger.domain.IAchievement;
import net.csongradyp.badger.event.IAchievementUnlockedEvent;

public interface IUnlockedProvider<TYPE extends IAchievement> {

    Optional<IAchievementUnlockedEvent> getUnlockable(TYPE achievement, Long score);
}
