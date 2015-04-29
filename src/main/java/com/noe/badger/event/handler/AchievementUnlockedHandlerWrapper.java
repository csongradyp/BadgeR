package com.noe.badger.event.handler;

import com.noe.badger.event.message.Achievement;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

@Listener(references = References.Strong)
public class AchievementUnlockedHandlerWrapper implements IAchievementUnlockedHandler {

    private final IAchievementUnlockedHandler wrapped;

    public AchievementUnlockedHandlerWrapper(final IAchievementUnlockedHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    @Handler(rejectSubtypes = true)
    public void onUnlocked(final Achievement achievement) {
        wrapped.onUnlocked(achievement);
    }

    public IAchievementUnlockedHandler getWrapped() {
        return wrapped;
    }
}
