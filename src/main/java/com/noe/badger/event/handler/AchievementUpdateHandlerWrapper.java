package com.noe.badger.event.handler;

import com.noe.badger.event.domain.Achievement;
import net.engio.mbassy.listener.Handler;

public class AchievementUpdateHandlerWrapper {

    private final IAchievementUnlockedHandler wrapped;

    public AchievementUpdateHandlerWrapper(final IAchievementUnlockedHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Handler(rejectSubtypes = true)
    public void onUnlocked(final Achievement achievement) {
        wrapped.onUnlocked(achievement);
    }
}
