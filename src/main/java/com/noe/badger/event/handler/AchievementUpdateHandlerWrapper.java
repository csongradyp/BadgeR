package com.noe.badger.event.handler;

import com.noe.badger.event.domain.Achievement;
import net.engio.mbassy.listener.Handler;

public class AchievementUpdateHandlerWrapper {

    private final IAchievementUpdateHandler wrapped;

    public AchievementUpdateHandlerWrapper(final IAchievementUpdateHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Handler(rejectSubtypes = true)
    public void onUpdate(final Achievement achievement) {
        wrapped.onUpdate(achievement);
    }
}
